package ce.daegu.ac.kr.aStartrip.service;

import ce.daegu.ac.kr.aStartrip.dto.ArticleDTO;
import ce.daegu.ac.kr.aStartrip.dto.MemberDTO;
import ce.daegu.ac.kr.aStartrip.entity.Article;
import ce.daegu.ac.kr.aStartrip.entity.Member;

import java.util.List;

public interface ArticleService {

    List<ArticleDTO> getAllArticleList();

    long AddArticle(MemberDTO memberDTO);

    default Article dtoToEntity(ArticleDTO dto, Member member) {
        Article entity = Article.builder()
                .num(dto.getNum())
                .title(dto.getTitle())
                .content(dto.getContent())
                .hit(dto.getHit())
                .type(dto.getType())
                .root(dto.getRoot())
                .member(member).build();
        return entity;
    }

    default ArticleDTO entityToDto(Article entity) {
        ArticleDTO dto = ArticleDTO.builder()
                .num(entity.getNum())
                .title(entity.getTitle())
                .writer(entity.getMember().getName())
                .content(entity.getContent())
                .hit(entity.getHit())
                .type(entity.getType())
                .root(entity.getRoot())
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .grade(entity.getMember().getGrade()).build();
        return dto;
    }
}
