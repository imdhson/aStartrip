package ce.daegu.ac.kr.aStartrip.handler;

import ce.daegu.ac.kr.aStartrip.dto.ArticleDTO;
import ce.daegu.ac.kr.aStartrip.dto.CardDTO;
import ce.daegu.ac.kr.aStartrip.dto.MemberDetails;
import ce.daegu.ac.kr.aStartrip.entity.Card;
import ce.daegu.ac.kr.aStartrip.repository.CardRepository;
import ce.daegu.ac.kr.aStartrip.service.ArticleService;
import ce.daegu.ac.kr.aStartrip.service.CardService;
import ce.daegu.ac.kr.aStartrip.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class CardWSHandler extends TextWebSocketHandler {
    private final ObjectMapper objectMapper;
    private final CardRepository cardRepository;
    private final CardService cardService;
    private final ArticleService articleService;
    private final MemberService memberService;

    private static Map<Long, List<WebSocketSession>> sessionList = new HashMap<>();
    private long key = 0;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String jsonPayload = message.getPayload();
        CardDTO cardDTO = objectMapper.readValue(jsonPayload, CardDTO.class);
        log.debug("WS 수신: {}", cardDTO);

        MemberDetails memberDetails = (MemberDetails) session.getAttributes().get("memberDetails");
        long articleId = cardService.getArticleId(cardDTO);
        articleService.updateCard1(memberDetails.getUsername(), articleId, cardDTO);

        //수정된 것을 받을 때마다 브로드캐스트로 card-ws   sendMessage 수행하여 js 에서 데이터 갱신하기
        Optional<Card> cardOptional = cardRepository.findById(cardDTO.getId());
        CardDTO cardDTO1 = cardService.entityToDto(cardOptional.get());
        session.sendMessage(new TextMessage(objectMapper.writeValueAsBytes(cardDTO1)));
    }
}

