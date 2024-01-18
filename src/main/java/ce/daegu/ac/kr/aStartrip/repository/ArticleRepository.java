package ce.daegu.ac.kr.aStartrip.repository;

import ce.daegu.ac.kr.aStartrip.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
