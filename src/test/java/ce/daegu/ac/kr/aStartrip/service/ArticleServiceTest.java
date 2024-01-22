package ce.daegu.ac.kr.aStartrip.service;

import ce.daegu.ac.kr.aStartrip.dto.CardDTO;
import ce.daegu.ac.kr.aStartrip.entity.Article;
import ce.daegu.ac.kr.aStartrip.entity.Card;
import ce.daegu.ac.kr.aStartrip.entity.CardTypeENUM;
import ce.daegu.ac.kr.aStartrip.entity.LLMStatusENUM;
import ce.daegu.ac.kr.aStartrip.repository.ArticleRepository;
import ce.daegu.ac.kr.aStartrip.repository.CardRepository;
import jakarta.persistence.Entity;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
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
    @Autowired
    private CardRepository cardRepository;

    @Test
    public void articleViewTest() {
        Optional<Article> articleOptional = articleRepository.findById(1L);
        if (articleOptional.isPresent()) {
            Article article = articleOptional.get();
            log.debug("article 출력 시작  ---- ");
            List<Card> articleCardList = article.getCardList();
            for (Card c : articleCardList) {
                log.debug("articleCardList - {} {} ", c.getId(), c.getUserInput0());
            }
        }
    }
}
