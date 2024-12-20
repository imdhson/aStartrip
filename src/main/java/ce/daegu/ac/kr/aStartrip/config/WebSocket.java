package ce.daegu.ac.kr.aStartrip.config;


import ce.daegu.ac.kr.aStartrip.broadcast.BroadcastService;
import ce.daegu.ac.kr.aStartrip.handler.ArticleWSHandler;
import ce.daegu.ac.kr.aStartrip.handler.CardWSHandler;
import ce.daegu.ac.kr.aStartrip.interceptor.HttpHandshakeInterceptor;
import ce.daegu.ac.kr.aStartrip.handler.TitleWSHandler;
import ce.daegu.ac.kr.aStartrip.repository.ArticleRepository;
import ce.daegu.ac.kr.aStartrip.service.ArticleService;
import ce.daegu.ac.kr.aStartrip.service.MemberService;
import ce.daegu.ac.kr.aStartrip.service.CardService;
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
    private final MemberService memberService;
    private final CardService cardService;
    private final BroadcastService broadcastService;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(titleWSHandler(), "/title-ws")
                .addInterceptors(new HttpHandshakeInterceptor()).setAllowedOrigins("*");
        registry.addHandler(cardWSHandler(), "/card-ws")
                .addInterceptors(new HttpHandshakeInterceptor()).setAllowedOrigins("*");
        registry.addHandler(articleWSHandler(), "/article-ws").setAllowedOrigins("*")
                .addInterceptors(new HttpHandshakeInterceptor()).setAllowedOrigins("*");
    }

    public WebSocketHandler titleWSHandler() {
        return new TitleWSHandler(objectMapper, articleRepository, articleService);
    }

    public WebSocketHandler cardWSHandler() {
        return new CardWSHandler(objectMapper, cardService, articleService, memberService, broadcastService);
    }

    public WebSocketHandler articleWSHandler() {
        return new ArticleWSHandler(objectMapper, articleRepository, articleService, cardService);
    }
}
