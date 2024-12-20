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

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String jsonPayload = message.getPayload();
        ArticleDTO articleDTO = objectMapper.readValue(jsonPayload, ArticleDTO.class);
        //log.debug("WS 수신: {}", articleDTO);

        if(!session.getAttributes().containsKey("key")) {
            session.getAttributes().put("key", articleDTO.getNum());
        }

        long key = (long)session.getAttributes().get("key");

        //log.info("before === key :  " + key + ", map : " + sessionListTitle);
        if(!sessionListTitle.containsKey(key)){
            sessionListTitle.put(key, new ArrayList<WebSocketSession>());
        }
        if(!sessionListTitle.get(key).contains(session)){
            sessionListTitle.get(key).add(session);
        }
        //log.info("after === key :  " + key + ", map : " + sessionListTitle);

        MemberDetails memberDetails = (MemberDetails) session.getAttributes().get("memberDetails");

        if(articleDTO.getTitle() != null) {
            boolean pass = articleService.updateArticle(memberDetails.getUsername(), articleDTO);

            if (pass) {
                //수정된 것을 받을 때마다 브로드캐스트로 title-ws 변경 sendMessage 수행하여 js 에서 데이터 갱신하기
                Optional<Article> articleOptional = articleRepository.findById(articleDTO.getNum());
                ArticleDTO articleDTO1 = articleService.entityToDto(articleOptional.get());
                for (WebSocketSession s : sessionListTitle.get(key)) {
                    s.sendMessage(new TextMessage(articleDTO1.getTitle()));
                }
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        long key = (long)session.getAttributes().get("key");
        sessionListTitle.get(key).remove(session);
        if(sessionListTitle.get(key).isEmpty() || sessionListTitle.get(key) == null) {
            sessionListTitle.remove(key);
        }
        //log.info("session remove === key :  " + key + ", map : " + sessionListTitle);

        super.afterConnectionClosed(session, status);
    }
}
