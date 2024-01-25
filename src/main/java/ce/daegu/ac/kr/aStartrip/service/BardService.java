package ce.daegu.ac.kr.aStartrip.service;

import ce.daegu.ac.kr.aStartrip.dto.CardDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class BardService implements LLMService{
    private final CardService cardService;
    @Override
    public CardDTO execute(CardDTO cardDTO) {
    CardDTO cardDTO1 = cardService.findCardById(cardDTO.getId());
    return cardDTO1; //수정해야함 !!!!
    }
}
