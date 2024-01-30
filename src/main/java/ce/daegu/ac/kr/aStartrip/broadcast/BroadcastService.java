package ce.daegu.ac.kr.aStartrip.broadcast;

import ce.daegu.ac.kr.aStartrip.dto.CardDTO;

public interface BroadcastService {
    void cardBroadcast(CardDTO dto, long key);
}
