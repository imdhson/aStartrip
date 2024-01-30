package ce.daegu.ac.kr.aStartrip.handler;

import ce.daegu.ac.kr.aStartrip.broadcast.BroadcastService;
import ce.daegu.ac.kr.aStartrip.dto.ArticleDTO;
import ce.daegu.ac.kr.aStartrip.dto.CardDTO;
import ce.daegu.ac.kr.aStartrip.dto.MemberDetails;
import ce.daegu.ac.kr.aStartrip.entity.Article;
import ce.daegu.ac.kr.aStartrip.entity.Card;
import ce.daegu.ac.kr.aStartrip.repository.CardRepository;
import ce.daegu.ac.kr.aStartrip.service.ArticleService;
import ce.daegu.ac.kr.aStartrip.service.CardService;
import ce.daegu.ac.kr.aStartrip.service.LLMService;
import ce.daegu.ac.kr.aStartrip.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class CardWSHandler extends TextWebSocketHandler {
    private final ObjectMapper objectMapper;
    //private final CardRepository cardRepository;
    private final CardService cardService;
    private final ArticleService articleService;
    private final MemberService memberService;
    private final BroadcastService broadcastService;

    private static Map<Long, List<WebSocketSession>> sessionListCard = new HashMap<>();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String jsonPayload = message.getPayload();
        CardDTO cardDTO = objectMapper.readValue(jsonPayload, CardDTO.class);
        //log.debug("WS 수신: {}", cardDTO);

        if (!session.getAttributes().containsKey("key")) {
            session.getAttributes().put("key", cardDTO.getId());
        }

        long key = (long) session.getAttributes().get("key");

        //log.info("before === key :  " + key + ", map : " + sessionListCard);
        if (!sessionListCard.containsKey(key)) {
            sessionListCard.put(key, new ArrayList<WebSocketSession>());
        }
        if (!sessionListCard.get(key).contains(session)) {
            sessionListCard.get(key).add(session);
        }
        //log.info("after === key :  " + key + ", map : " + sessionListCard);


        MemberDetails memberDetails = (MemberDetails) session.getAttributes().get("memberDetails");

        if (cardDTO.getCardType() != null) {
            long articleId = cardService.getArticleId(cardDTO);
            boolean pass = articleService.updateCard1(memberDetails.getUsername(), articleId, cardDTO, key);


            if (pass) {
                //수정된 것을 받을 때마다 브로드캐스트로 card-ws   sendMessage 수행하여 js 에서 데이터 갱신하기
                CardDTO cardDTO1 = cardService.findCardById(cardDTO.getId());
                broadcastService.cardBroadcast(cardDTO1, key);
            }
        }
    }

    public static Map<Long, List<WebSocketSession>> getSessionList() {
        return sessionListCard;
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        long key = (long) session.getAttributes().get("key");
        sessionListCard.get(key).remove(session);
        if (sessionListCard.get(key).isEmpty() || sessionListCard.get(key) == null) {
            sessionListCard.remove(key);
        }
        //log.info("session remove === key :  " + key + ", map : " + sessionListCard);

        super.afterConnectionClosed(session, status);
    }
}

