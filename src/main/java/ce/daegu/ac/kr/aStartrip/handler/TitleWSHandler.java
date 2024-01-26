package ce.daegu.ac.kr.aStartrip.handler;

import ce.daegu.ac.kr.aStartrip.dto.ArticleDTO;
import ce.daegu.ac.kr.aStartrip.dto.MemberDetails;
import ce.daegu.ac.kr.aStartrip.entity.Article;
import ce.daegu.ac.kr.aStartrip.repository.ArticleRepository;
import ce.daegu.ac.kr.aStartrip.service.ArticleService;
import ce.daegu.ac.kr.aStartrip.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class TitleWSHandler extends TextWebSocketHandler {
    private final ObjectMapper objectMapper;
    private final ArticleRepository articleRepository;
    private final ArticleService articleService;

    private static Map<Long, List<WebSocketSession>> sessionListTitle = new HashMap<>();
    private static Map<WebSocketSession, Long> keyList = new HashMap<>();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String jsonPayload = message.getPayload();
        ArticleDTO articleDTO = objectMapper.readValue(jsonPayload, ArticleDTO.class);
        log.debug("WS 수신: {}", articleDTO);

        if(!keyList.containsKey(session)) {
            keyList.put(session, articleDTO.getNum());
        }

        log.info("before === key :  " + keyList.get(session) + ", map : " + sessionListTitle);
        if(!sessionListTitle.containsKey(keyList.get(session))){
            sessionListTitle.put(keyList.get(session), new ArrayList<WebSocketSession>());
        }
        if(!sessionListTitle.get(keyList.get(session)).contains(session)){
            sessionListTitle.get(keyList.get(session)).add(session);
        }
        log.info("after === key :  " + keyList.get(session) + ", map : " + sessionListTitle);

        MemberDetails memberDetails = (MemberDetails) session.getAttributes().get("memberDetails");

        if(articleDTO.getTitle() != null) {
            boolean pass = articleService.updateArticle(memberDetails.getUsername(), articleDTO);

            if (pass) {
                //수정된 것을 받을 때마다 브로드캐스트로 title-ws 변경 sendMessage 수행하여 js 에서 데이터 갱신하기
                Optional<Article> articleOptional = articleRepository.findById(articleDTO.getNum());
                ArticleDTO articleDTO1 = articleService.entityToDto(articleOptional.get());
                for (WebSocketSession s : sessionListTitle.get(keyList.get(session))) {
                    s.sendMessage(new TextMessage(articleDTO1.getTitle()));
                }
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessionListTitle.get(keyList.get(session)).remove(session);
        if(sessionListTitle.get(keyList.get(session)).isEmpty() || sessionListTitle.get(keyList.get(session)) == null) {
            sessionListTitle.remove(keyList.get(session));
        }
        log.info("session remove === key :  " + keyList.get(session) + ", map : " + sessionListTitle);
        keyList.remove(session);

        super.afterConnectionClosed(session, status);
    }
}
