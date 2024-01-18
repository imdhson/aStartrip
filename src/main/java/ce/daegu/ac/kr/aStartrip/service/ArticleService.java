package ce.daegu.ac.kr.aStartrip.service;

import ce.daegu.ac.kr.aStartrip.dto.ArticleDTO;
import ce.daegu.ac.kr.aStartrip.entity.Article;

import java.util.List;

public interface ArticleService {

    List<ArticleDTO> getAllArticleList();

    default Article dtoToEntity(ArticleDTO dto) {
        Article entity = Article.builder()
                .num(dto.getNum())
                .title(dto.getTitle())
                .writer(dto.getWriter())
                .content(dto.getContent())
                .hit(dto.getHit())
                .type(dto.getType())
                .root(dto.getRoot())
                .grade(dto.getGrade()).build();
        return entity;
    }

    default ArticleDTO entityToDto(Article entity) {
        ArticleDTO dto = ArticleDTO.builder()
                .num(entity.getNum())
                .title(entity.getTitle())
                .writer(entity.getWriter())
                .content(entity.getContent())
                .hit(entity.getHit())
                .type(entity.getType())
                .root(entity.getRoot())
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .grade(entity.getGrade()).build();
        return dto;
    }
}
