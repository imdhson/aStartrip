//package ce.daegu.ac.kr.aStartrip.service;
//
//import ce.daegu.ac.kr.aStartrip.broadcast.BroadcastService;
//import ce.daegu.ac.kr.aStartrip.dto.CardDTO;
//import ce.daegu.ac.kr.aStartrip.entity.Card;
//import ce.daegu.ac.kr.aStartrip.entity.LLMStatusENUM;
//import ce.daegu.ac.kr.aStartrip.repository.CardRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.util.Optional;
//
//@RequiredArgsConstructor
//@Service
//@Slf4j
//@Component
//public class LLMServiceBard implements LLMService {
//    private final CardRepository cardRepository;
//    private final BroadcastService broadcastService;
//
//    @Value("${python.path}")
//    private String pythonPath;
//
//    public static boolean running = false;
//
//    @Override
//    public boolean execute(Card entity) {
//        while (running) {
//            try {
//                Thread.sleep(6000); //6초 대기
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        }
//
//        try {
//            Thread.sleep(2000); //2초 대기 어뷰징 방지용
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//
//        log.debug("현재 디렉터리:{}", System.getProperty("user.dir"));
//
//        try {
//            running = true;
//            Process process = new ProcessBuilder
//                    (pythonPath, "./src/main/bard.py",
//                            String.valueOf(entity.getId())).start();
//            // $ python ./src/main/bard.py 카드ID
//            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//            String line;
//            while ((line = reader.readLine()) != null) {
//                log.debug("Bard python: {}", line);
//                if (line.trim().equals("true")) {
//                    log.debug("Bard 성공");
//                    running = false;
//                    return true;
//                }
//            }
//        } catch (IOException e) {
//            entity.setLlmStatus(LLMStatusENUM.CANCELED);
//            cardRepository.save(entity);
//            throw new RuntimeException(e);
//        }
//        log.debug("Bard 실패");
//        entity.setLlmStatus(LLMStatusENUM.CANCELED);
//        cardRepository.save(entity);
//        running = false;
//        return false;
//    }
//
//    @Override
//    public void completeWating(CardDTO dto, long key) {
//        while (true) {
//            try {
//                Thread.sleep(3000); //3초 대기
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//            if (dto.getLlmStatus() == LLMStatusENUM.COMPLETED || dto.getLlmStatus() == LLMStatusENUM.CANCELED) {
//                broadcastService.cardBroadcast(dto, key);
//                break;
//            }
//        }
//    }
//}
