package ce.daegu.ac.kr.aStartrip.service;

import ce.daegu.ac.kr.aStartrip.dto.ArticleDTO;
import ce.daegu.ac.kr.aStartrip.dto.MemberDTO;
import ce.daegu.ac.kr.aStartrip.entity.Article;
import ce.daegu.ac.kr.aStartrip.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface ArticleService {
    List<ArticleDTO> getAllArticleList();

    long addArticle(Member member);

    default Article dtoToEntity(ArticleDTO dto, Member member) {
        Article entity = Article.builder()
                .num(dto.getNum())
                .title(dto.getTitle())
                .content(dto.getContent())
                .member(member)
                .hit(dto.getHit())
                .visibleBoard(dto.isVisibleBoard())
                .member(member)
                .build();
        return entity;
    }

    default ArticleDTO entityToDto(Article entity) {
        ArticleDTO dto = ArticleDTO.builder()
                .num(entity.getNum())
                .title(entity.getTitle())
                .writer(entity.getMember().getName())
                .content(entity.getContent())
                .hit(entity.getHit())
                .visibleBoard(entity.isVisibleBoard())
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate()).build();
        return dto;
    }
}
