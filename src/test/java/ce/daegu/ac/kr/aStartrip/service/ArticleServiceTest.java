package ce.daegu.ac.kr.aStartrip.service;

import ce.daegu.ac.kr.aStartrip.dto.CardDTO;
import ce.daegu.ac.kr.aStartrip.entity.*;
import ce.daegu.ac.kr.aStartrip.repository.ArticleRepository;
import ce.daegu.ac.kr.aStartrip.repository.CardRepository;
import ce.daegu.ac.kr.aStartrip.repository.MemberRepository;
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
    @Autowired
    private MemberRepository memberRepository;

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

    @Test
    public void articleInsertTest() {
        Member member = memberRepository.findById(("1@1.com")).get();
        for (int i = 0; i < 30; i++) {
            Article article = Article.builder().articlePermission(ArticlePermissionENUM.OPEN).
                    member(member).
                    hit(10).
                    title(String.valueOf(i) + "test added")
                    .build();
            articleRepository.save(article);
        }

    }
}
