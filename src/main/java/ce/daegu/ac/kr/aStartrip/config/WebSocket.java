package ce.daegu.ac.kr.aStartrip.config;


import ce.daegu.ac.kr.aStartrip.handler.ArticleWSHandler;
import ce.daegu.ac.kr.aStartrip.handler.CardWSHandler;
import ce.daegu.ac.kr.aStartrip.repository.ArticleRepository;
import ce.daegu.ac.kr.aStartrip.service.ArticleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocket implements WebSocketConfigurer {
    private final ObjectMapper objectMapper;
    private final ArticleRepository articleRepository;
    private final ArticleService articleService;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(articleWSHandler(), "/article-ws").setAllowedOrigins("*");
        registry.addHandler(cardWSHandler(), "/card-ws").setAllowedOrigins("*");
    }

    public WebSocketHandler articleWSHandler() {
        return new ArticleWSHandler(objectMapper, articleRepository, articleService);
    }

    public WebSocketHandler cardWSHandler() {
        return new CardWSHandler(objectMapper);
    }
}
