package ce.daegu.ac.kr.aStartrip.repository;

import ce.daegu.ac.kr.aStartrip.dto.CardDTO;
import ce.daegu.ac.kr.aStartrip.entity.*;
import ce.daegu.ac.kr.aStartrip.service.CardService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@Slf4j
@SpringBootTest
public class ArticleInsertTest {
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private CardService cardService;

    @Test
    public void articleInsertTest() {
        Optional<Member> member = memberRepository.findById("test@gmail.com");
        Member m = member.get();
        Article article = Article.builder()
                .hit(0)
                .title("자동생성")
                .member(m)
                .build();
        articleRepository.save(article);
    }

    @Test
    public void articleCardAddTest() {
        Optional<Article> articleOptional = articleRepository.findById(1L);
        if (articleOptional.isPresent()) {
            Article article = articleOptional.get();
            log.debug("DEBUG before::::" + article);
            CardDTO cardDTO = CardDTO.builder()
                    .cardType(CardTypeENUM.W01)
                    .UserInput0("안녕하세요. 구글 바드님. 대구대학교 이백승에 대해 알려주세요.")
                    .LLMResponse0("LLMResponse0")
                    .LLMResponse1("LLMResponse1")
                    .LLMResponse2("LLMResponse2")
                    .llmStatus(LLMStatusENUM.GENERATING)
                    .build();
            cardService.addCard(1L, cardDTO);

//            Card card = Card.builder()
//                    .cardType(CardTypeENUM.R01)
//                    .UserInput0("111111")
//                    .article(article)
//                    .llmStatus(LLMStatusENUM.NEW)
//                    .LLMResponse0(":1312312321312")
//                    .build();
//            cardRepository.save(card);
        }

    }
}
