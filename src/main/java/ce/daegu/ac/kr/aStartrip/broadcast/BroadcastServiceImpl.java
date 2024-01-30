package ce.daegu.ac.kr.aStartrip.broadcast;

import ce.daegu.ac.kr.aStartrip.dto.CardDTO;
import ce.daegu.ac.kr.aStartrip.handler.CardWSHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class BroadcastServiceImpl implements BroadcastService{

    private final ObjectMapper objectMapper;

    @Override
    public void cardBroadcast(CardDTO dto, long key) {
        for (WebSocketSession s : CardWSHandler.getSessionList().get(key)) {
            try {
                s.sendMessage(new TextMessage(objectMapper.writeValueAsBytes(dto)));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
