package ce.daegu.ac.kr.aStartrip.handler;

import ce.daegu.ac.kr.aStartrip.dto.ArticleDTO;
import ce.daegu.ac.kr.aStartrip.entity.Article;
import ce.daegu.ac.kr.aStartrip.repository.ArticleRepository;
import ce.daegu.ac.kr.aStartrip.service.ArticleService;
import ce.daegu.ac.kr.aStartrip.service.MemberService;
import ce.daegu.ac.kr.aStartrip.service.MemberServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ArticleWSHandler extends TextWebSocketHandler {
    private final ObjectMapper objectMapper;
    private final ArticleRepository articleRepository;
    private final ArticleService articleService;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String jsonPayload = message.getPayload();

        //card add 시에
        // 브로드캐스트로 sendMessage 수행하여 js 에서 데이터 갱신하기
//        Optional<Article> articleOptional= articleRepository.findById(articleDTO.getNum());
//        ArticleDTO articleDTO1 = articleService.entityToDto(articleOptional.get());
//        session.sendMessage(new TextMessage(objectMapper.writeValueAsBytes(articleDTO1)));
    }

}
