package ce.daegu.ac.kr.aStartrip.handler;

import ce.daegu.ac.kr.aStartrip.dto.ArticleDTO;
import ce.daegu.ac.kr.aStartrip.dto.MemberDetails;
import ce.daegu.ac.kr.aStartrip.entity.Article;
import ce.daegu.ac.kr.aStartrip.repository.ArticleRepository;
import ce.daegu.ac.kr.aStartrip.service.ArticleService;
import ce.daegu.ac.kr.aStartrip.service.CardService;
import ce.daegu.ac.kr.aStartrip.service.MemberService;
import ce.daegu.ac.kr.aStartrip.service.MemberServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class ArticleWSHandler extends TextWebSocketHandler {
    private final ObjectMapper objectMapper;
    private final ArticleRepository articleRepository;
    private final ArticleService articleService;
    private final CardService cardService;

    private static Map<Long, List<WebSocketSession>> sessionListArticle = new HashMap<>();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String jsonPayload = message.getPayload();
        ArticleDTO articleDTO = objectMapper.readValue(jsonPayload, ArticleDTO.class);
        log.info("WS 수신 : " + articleDTO);

        if (!session.getAttributes().containsKey("key")) {
            session.getAttributes().put("key", articleDTO.getNum());
        }

        long key = (long) session.getAttributes().get("key");

        log.info("Article before === key :  " + key + ", map : " + sessionListArticle);
        if (!sessionListArticle.containsKey(key)) {
            sessionListArticle.put(key, new ArrayList<WebSocketSession>());
        }
        if (!sessionListArticle.get(key).contains(session)) {
            sessionListArticle.get(key).add(session);
        }
        log.info("Article after === key :  " + key + ", map : " + sessionListArticle);

        MemberDetails memberDetails = (MemberDetails) session.getAttributes().get("memberDetails");
        Optional<Article> articleOptional1 = articleRepository.findById(articleDTO.getNum());
        ArticleDTO articleDTO1 = articleService.entityToDto(articleOptional1.get());

        if (articleDTO.getCardDTOList() != null && articleDTO1.getWriter_email().equals(memberDetails.getUsername())) { // 맨 처음 게시글에 접근할 때 list가 없는 articleDTO를 받아 오류 발생
            boolean pass = cardService.addCard(articleDTO.getNum(), articleDTO.getCardDTOList().get(0)); // 추가되는 카드의 갯수는 언제나 하나
            if (pass) {
                // 브로드캐스트로 sendMessage 수행하여 js 에서 데이터 갱신하기
                for (WebSocketSession s : sessionListArticle.get(key)) {
                    Optional<Article> articleOptional2 = articleRepository.findById(articleDTO.getNum());
                    ArticleDTO articleDTO2 = articleService.entityToDto(articleOptional2.get());
                    s.sendMessage(new TextMessage(objectMapper.writeValueAsBytes(articleDTO2)));
                }
                // articleDTO 내에 cardDTOList가 존재하므로 가능.

            }
        }
        if (articleDTO.getArticlePermission() != null) {
            boolean pass = articleService.updateArticle(memberDetails.getUsername(), articleDTO);
            if (pass) {
                // 브로드캐스트로 sendMessage 수행하여 js 에서 데이터 갱신하기
                Optional<Article> articleOptional2 = articleRepository.findById(articleDTO.getNum());
                ArticleDTO articleDTO2 = articleService.entityToDto(articleOptional2.get());
                for (WebSocketSession s : sessionListArticle.get(key)) {
                    s.sendMessage(new TextMessage(objectMapper.writeValueAsBytes(articleDTO2)));
                }
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        long key = (long) session.getAttributes().get("key");
        sessionListArticle.get(key).remove(session);
        if (sessionListArticle.get(key).isEmpty() || sessionListArticle.get(key) == null) {
            sessionListArticle.remove(key);
        }
        log.info("session remove === key :  " + key + ", map : " + sessionListArticle);

        super.afterConnectionClosed(session, status);
    }
}
