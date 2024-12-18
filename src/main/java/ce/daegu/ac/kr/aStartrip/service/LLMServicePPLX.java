package ce.daegu.ac.kr.aStartrip.service;

import ce.daegu.ac.kr.aStartrip.broadcast.BroadcastService;
import ce.daegu.ac.kr.aStartrip.dto.CardDTO;
import ce.daegu.ac.kr.aStartrip.entity.Card;
import ce.daegu.ac.kr.aStartrip.entity.CardTypeENUM;
import ce.daegu.ac.kr.aStartrip.entity.LLMStatusENUM;
import ce.daegu.ac.kr.aStartrip.repository.CardRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
@Component
public class LLMServicePPLX implements LLMService {
    private final CardRepository cardRepository;
    private final BroadcastService broadcastService;

    @Value("${pplx.api.token}")
    private String pplxApiToken;

    public static boolean running = false;

    @Override
    public boolean execute(Card entity) {
        while (running) {
            try {
                Thread.sleep(6000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            Thread.sleep(2000); //2초 대기 어뷰징 방지용
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        //main 로직
        running = true;
        String request_i;
        CardTypeENUM card_type = entity.getCardType();
        switch (card_type) {
            case R01:
                request_i = """
                        I need to study English. Please generate English paragraph on any topic. It can be long
                        """;
                entity.setLLMResponse0(requestToAPI(request_i));
                entity.setLlmStatus(LLMStatusENUM.COMPLETED);
                break;
            case R02:
//                        Pre-reading question
                request_i = """
                        Please create a pre-reading question for the paragraph below. Please provide answers in English and Korean.
                        ________
                        
                        """ + entity.getUserInput0();
                entity.setLLMResponse0(requestToAPI(request_i));
                entity.setLlmStatus(LLMStatusENUM.COMPLETED);
//            Backgrond-knowledge
                request_i = """
                        Please create a background-knowledge for the paragraph below. Please provide answers in English and Korean.
                        ________
                        
                        """ + entity.getUserInput0();
                entity.setLLMResponse1(requestToAPI(request_i));
                entity.setLlmStatus(LLMStatusENUM.COMPLETED);
//            Post-reading question
                request_i = """
                        Please create a post-reading question for the paragraph below. Please provide answers in English and Korean.
                        ________
                        
                        """ + entity.getUserInput0();
                entity.setLLMResponse2(requestToAPI(request_i));
                entity.setLlmStatus(LLMStatusENUM.COMPLETED);
            case W01:
                request_i = """
                        I'm studying English. I'll send you a paragraph, so please evaluate whether the paragraph I sent is appropriate and correct it. Please reply in Korean.
                        _________
                        
                        """ + entity.getUserInput0();
                entity.setLLMResponse0(requestToAPI(request_i));
                entity.setLlmStatus(LLMStatusENUM.COMPLETED);
                break;
            case W02:
                request_i = """
                        I'm studying English. I'll send you a sentence, so please analyze the composition of the sentence. Please reply in Korean.
                        __________
                        
                        """ + entity.getUserInput0();
                entity.setLLMResponse0(requestToAPI(request_i));
                entity.setLlmStatus(LLMStatusENUM.COMPLETED);
                break;
            case V01:
                request_i = """
                        I am studying English. I'll send you the words separated by , so please create an English story using the words.
                        _________
                        words: 
                        """ + entity.getUserInput0();
                entity.setLLMResponse0(requestToAPI(request_i));
                entity.setLlmStatus(LLMStatusENUM.COMPLETED);
                break;
            case V02:
//                       동의어
                request_i = """
                        I am studying English. I'll send you the word, so please find the relevant synonyms word. Find the word for english. and explain in Korean. 
                        단어를 줄테니 relevant synonyms 찾아주세요. 아래는 단어 입니다.
                        ________
                        
                        """ + entity.getUserInput0();
                entity.setLLMResponse0(requestToAPI(request_i));
//            반의어
                request_i = """
                        I am studying English. I'll send you the sentence, so please find the related antonym word. Find the word for english. and explain in Korean.
                        단어를 줄테니 antonym 찾아주세요. 아래는 단어 입니다.
                        ________
                        
                        """ + entity.getUserInput0();
                entity.setLLMResponse1(requestToAPI(request_i));
//            word-family
                request_i = """
                        I am studying English. I'll send you the sentence, so please find the word-family. Find the word for english. and explain in Korean.
                        단어를 줄테니 Word-family 찾아주세요. 아래는 단어 입니다.
                        ________
                        
                        """ + entity.getUserInput0();
                entity.setLLMResponse2(requestToAPI(request_i));
                entity.setLlmStatus(LLMStatusENUM.COMPLETED);
                break;
            default:
                entity.setLlmStatus(LLMStatusENUM.CANCELED);
                running = false;
                cardRepository.save(entity);
                return false;
        }

        running = false;
        cardRepository.save(entity);
        return true;
    }

    @Override
    public void completeWaiting(CardDTO dto, long key) {
        while (true) {
            try {
                Thread.sleep(3000); //3초 대기
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (dto.getLlmStatus() == LLMStatusENUM.COMPLETED || dto.getLlmStatus() == LLMStatusENUM.CANCELED) {
                broadcastService.cardBroadcast(dto, key);
                break;
            }
        }
    }

    public String requestToAPI(String request_i) {
        String token = "Bearer " + pplxApiToken;
        String requestBody = buildRequestBody(request_i);
        HttpResponse<String> response = null;

        try {
            response = Unirest.post("https://api.perplexity.ai/chat/completions").header("Authorization", token).header("Content-Type", "application/json").body(requestBody).asString();
        } catch (UnirestException e) {
            throw new RuntimeException(e);
        }
        return extractContent(response.getBody());
    }

    public String buildRequestBody(String request_i) {
        Map<String, Object> body = new HashMap<>();
        body.put("model", "llama-3.1-sonar-small-128k-online");
        body.put("messages", List.of(Map.of("role", "system", "content", "Be precise and concise."), Map.of("role", "user", "content", request_i)));
        body.put("temperature", 0.2);
        body.put("top_p", 0.9);
        body.put("return_citations", true);
        body.put("search_domain_filter", List.of("perplexity.ai"));
        body.put("return_images", false);
        body.put("return_related_questions", false);
        body.put("search_recency_filter", "month");
        body.put("top_k", 0);
        body.put("stream", false);
        body.put("presence_penalty", 0);
        body.put("frequency_penalty", 1);

        return new JSONObject(body).toString();
    }

    public static String extractContent(String jsonResponse) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);

            JsonNode contentNode = rootNode.path("choices")
                    .path(0)
                    .path("message")
                    .path("content");

            if (contentNode.isMissingNode()) {
                return "Content not found in the response";
            }

            return contentNode.asText();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error parsing JSON response";
        }
    }
}
