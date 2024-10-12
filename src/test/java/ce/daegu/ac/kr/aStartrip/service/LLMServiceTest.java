package ce.daegu.ac.kr.aStartrip.service;

import ce.daegu.ac.kr.aStartrip.repository.CardRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.http.HttpResponse;

@SpringBootTest
@Slf4j
public class LLMServiceTest {
    @Autowired private CardRepository cardRepository;
    @Autowired
    private LLMService llmService;

    @Test
    public void executeTest(){
        log.debug("executeTest() 시작.");
        boolean result = llmService.execute(cardRepository.findById(1L).get());
        log.debug("executeTest() 결과: {}", result);
    }

    @SneakyThrows
    @Test
    public void apiTest(){
        log.debug("apiTest() 시작");
        String r = llmService.requestToAPI("오늘의 요리는 뭔가요?");
        log.debug("r = "+ r);
    }

}