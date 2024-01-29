package ce.daegu.ac.kr.aStartrip.service;

import ce.daegu.ac.kr.aStartrip.entity.Card;
import ce.daegu.ac.kr.aStartrip.entity.LLMStatusENUM;
import ce.daegu.ac.kr.aStartrip.repository.CardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@RequiredArgsConstructor
@Service
@Slf4j
public class LLMServiceBard implements LLMService {
    private final CardRepository cardRepository;

    public static boolean running = false;

    @Override
    public boolean execute(Card entity) {

        if (running) { //running 중일 때
            return false;
        }
        try {
            Thread.sleep(3000); //3초 대기
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.debug("현재 디렉터리:{}", System.getProperty("user.dir"));

        try {
            Process process = new ProcessBuilder
                    ("python", "./src/main/bard.py",
                            String.valueOf(entity.getId())).start();
            // $ python ./src/main/bard.py 카드ID
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                log.debug("Bard python: {}", line);
                if (line.trim().equals("true")) {
                    log.debug("Bard 성공");
                    return true;
                } else {
                    log.debug("Bard 실패");
                    entity.setLlmStatus(LLMStatusENUM.CANCELED);
                    cardRepository.save(entity);
                    return false;
                }
            }
        } catch (IOException e) {
            entity.setLlmStatus(LLMStatusENUM.CANCELED);
            cardRepository.save(entity);
            throw new RuntimeException(e);
        }

        entity.setLlmStatus(LLMStatusENUM.CANCELED);
        cardRepository.save(entity);
        return false;
    }
}
