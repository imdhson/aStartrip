package ce.daegu.ac.kr.aStartrip.service;

import ce.daegu.ac.kr.aStartrip.dto.CardDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.Buffer;

@RequiredArgsConstructor
@Service
@Slf4j
public class BardService implements LLMService {
    private final CardService cardService;

    public static boolean running = false;

    @Override
    public boolean execute(long card_id) {

        if (running) { //running 중일 때
            return false;
        }

        log.debug("현재 디렉터리:{}", System.getProperty("user.dir"));

        try {
            Process process = new ProcessBuilder
                    ("python", "./src/main/bard.py",
                            String.valueOf(card_id)).start();
            // $ python ./src/main/bard.py 카드ID
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                log.debug("line: {}", line);
                if (line.trim().equals("true")) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return false;
    }
}
