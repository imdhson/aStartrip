package ce.daegu.ac.kr.aStartrip.controller;

import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebSocketController {
    @MessageMapping("/chat") // 클라이언트가 /app/chat으로 메세지를 보낼 때 호출
    @SendTo("/topic/messages") // /topic/message로 메세지를 브로드캐스트
    public Message sendMessage(Message message) {
        return message;
    }

    @GetMapping("/board")
    public String board(Model model, Authentication authentication) {
        return "chat";
    }
}
