package ce.daegu.ac.kr.aStartrip.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@SpringBootTest
@Slf4j
public class LLMServiceTest {
    @Autowired
    private LLMService llmService;

    @Test
    public void executeTest(){
        log.debug("executeTest() 시작.");
        boolean result = llmService.execute(2L);
        log.debug("executeTest() 결과: {}", result);
    }

}
