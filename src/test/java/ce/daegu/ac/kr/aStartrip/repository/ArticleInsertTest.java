package ce.daegu.ac.kr.aStartrip.repository;

import ce.daegu.ac.kr.aStartrip.entity.Article;
import ce.daegu.ac.kr.aStartrip.entity.Member;
import jakarta.transaction.Transactional;
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

    @Test
    public void articleInsertTest() {
        Optional<Member> member = memberRepository.findById("1@1.com");

        Article article = Article.builder()
                .hit(0)
                .title("자동생성")
                .type('a')
                .member(member.get())
                .content("자동생성")
                .build();
        articleRepository.save(article);
    }
}
