package ce.daegu.ac.kr.aStartrip.service;

import ce.daegu.ac.kr.aStartrip.dto.CardDTO;
import ce.daegu.ac.kr.aStartrip.entity.Card;
import com.mashape.unirest.http.exceptions.UnirestException;

public interface LLMService {
    public boolean execute(Card entity);
    //이 함수 호출시 비동기로 처리하는 것 권장

    void completeWaiting(CardDTO dto, long key);

    String requestToAPI(String request_i) throws Exception;
}
