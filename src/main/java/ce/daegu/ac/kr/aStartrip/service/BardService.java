package ce.daegu.ac.kr.aStartrip.service;

import ce.daegu.ac.kr.aStartrip.dto.CardDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class BardService implements LLMService {
    private final CardService cardService;

    @Override
    public CardDTO execute(CardDTO cardDTO) {
        //carddto.id 로 llmstatus 가 generating 인 경우에
        // userinput0으로 llm 사용 이후 llmresponse에 반환함.
        CardDTO cardDTO1 = cardService.findCardById(cardDTO.getId());
        return cardDTO1; //수정해야함 !!!!
    }
}
