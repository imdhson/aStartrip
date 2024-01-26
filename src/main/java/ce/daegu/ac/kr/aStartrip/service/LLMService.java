package ce.daegu.ac.kr.aStartrip.service;

import ce.daegu.ac.kr.aStartrip.dto.CardDTO;

public interface LLMService {
    boolean execute(long card_id);
    //이 함수 호출시 비동기로 처리하는 것 권장
}
