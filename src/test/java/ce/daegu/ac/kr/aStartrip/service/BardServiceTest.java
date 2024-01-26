package ce.daegu.ac.kr.aStartrip.service;

import ce.daegu.ac.kr.aStartrip.dto.CardDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.parameters.P;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@SpringBootTest
@Slf4j
public class BardServiceTest {
    @Autowired
    private LLMService llmService;
    @Autowired
    private CardService cardService;


    @Test
    public void currentpath(){
        llmService.execute(1);
    }

    @Test
    public void executeTest() {

//        CardDTO cardDTO  = cardService.findCardById(1L);
//        llmService.execute(cardDTO);
        try{
            String command = "python";
            String command1 = "--version";
            //명령어 설정
            Process process = new ProcessBuilder(command, command1).start();

            //명령어 출력 읽기
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line= reader.readLine()) != null){
                log.debug(line);
            }

            //프로세스 종료 코드 확인
            int exitCode = process.waitFor();
            log.debug("종료: {}", exitCode);


        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
