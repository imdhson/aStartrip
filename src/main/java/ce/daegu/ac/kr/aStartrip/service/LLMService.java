package ce.daegu.ac.kr.aStartrip.service;

import ce.daegu.ac.kr.aStartrip.dto.CardDTO;
import ce.daegu.ac.kr.aStartrip.entity.Card;

public interface LLMService {
    public boolean execute(Card entity);
    //이 함수 호출시 비동기로 처리하는 것 권장

    void completeWating(CardDTO dto, long key);
}
