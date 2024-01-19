package ce.daegu.ac.kr.aStartrip.repository;

import ce.daegu.ac.kr.aStartrip.entity.Article;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class ArticleInsertTest {
    @Autowired
    private ArticleRepository articleRepository;

    @Test
    public void articleInsertTest() {
        Article article = Article.builder()
                .hit(0)
                .title("자동생성")
                .grade('a')
                .type('a')
                .writer("11")
                .content("자동생성")
                .build();
        articleRepository.save(article);
    }
}
