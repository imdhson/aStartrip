package ce.daegu.ac.kr.aStartrip.repository;

import ce.daegu.ac.kr.aStartrip.entity.Card;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CardRepositoryTest {
    @Autowired
    private CardRepository cardRepository;
    @Test
    public void delCard(){
        Card card = cardRepository.findById(2L).get();
        cardRepository.delete(card);
    }
}
