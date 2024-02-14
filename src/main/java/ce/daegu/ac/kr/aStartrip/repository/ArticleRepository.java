package ce.daegu.ac.kr.aStartrip.repository;

import ce.daegu.ac.kr.aStartrip.entity.Article;
import ce.daegu.ac.kr.aStartrip.entity.ArticlePermissionENUM;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    Page<Article> findByArticlePermissionOrderByHitDesc(ArticlePermissionENUM articlePermissionENUM, Pageable pageable);
    //받은 매개변수와 일치하는 것을 반환하게 됨. 그리고 hit을 기준으로 내림차순 출력

    @Query("SELECT a FROM Article a " +
            "WHERE (LOWER(a.title) LIKE LOWER(CONCAT('%', :inputQuery, '%')) " +
            "OR LOWER(a.member.name) LIKE LOWER(CONCAT('%', :inputQuery, '%')) " +
            "OR LOWER(a.member.email) LIKE LOWER(CONCAT('%', :inputQuery, '%'))) " +
            "AND a.articlePermission = 'OPEN'")
    Page<Article> findByTitleContainingOrMemberEmailContaining(@Param("inputQuery") String inputQuery, Pageable pageable);
    //제목 OR 작성자 OR 이메일로 검색
    // AND 게시글권한open 인 상태만 검색,
    // concat은 문자열 이어붙이는 역할임. LOWER는 lowercase임.
}
