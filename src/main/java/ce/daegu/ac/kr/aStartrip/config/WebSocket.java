package ce.daegu.ac.kr.aStartrip.config;


import ce.daegu.ac.kr.aStartrip.handler.ArticleWSHandler;
import ce.daegu.ac.kr.aStartrip.handler.CardWSHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocket implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(articleWSHandler(), "/article-ws").setAllowedOrigins("*");
        registry.addHandler(cardWSHandler(), "/card-ws").setAllowedOrigins("*");
    }

    public WebSocketHandler articleWSHandler() {
        return new ArticleWSHandler(new ObjectMapper());
    }

    public WebSocketHandler cardWSHandler() {
        return new CardWSHandler(new ObjectMapper());
    }
}
