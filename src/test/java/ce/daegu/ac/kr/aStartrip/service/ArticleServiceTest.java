package ce.daegu.ac.kr.aStartrip.service;

import ce.daegu.ac.kr.aStartrip.dto.CardDTO;
import ce.daegu.ac.kr.aStartrip.entity.Article;
import ce.daegu.ac.kr.aStartrip.entity.CardTypeENUM;
import ce.daegu.ac.kr.aStartrip.entity.LLMStatusENUM;
import ce.daegu.ac.kr.aStartrip.repository.ArticleRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
@Slf4j
@Transactional
public class ArticleServiceTest {

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private CardService cardService;

    @Test
    public void articleCardAddTest() {
        Optional<Article> articleOptional = articleRepository.findById(1L);
        if (articleOptional.isPresent()) {
            Article article = articleOptional.get();
            log.debug("DEBUG before::::" + article);
            CardDTO cardDTO = CardDTO.builder()
                    .cardType(CardTypeENUM.R01)
                    .UserInput0("123123123")
                    .LLMResponse0("123123123123123")
                    .llmStatus(LLMStatusENUM.COMPLETED)
                    .build();

            boolean temp = cardService.addCard(1L, cardDTO);
            log.debug("11111111" + temp);

        }
    }
}
