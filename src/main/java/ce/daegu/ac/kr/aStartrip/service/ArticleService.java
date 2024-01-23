package ce.daegu.ac.kr.aStartrip.service;

import ce.daegu.ac.kr.aStartrip.dto.ArticleDTO;
import ce.daegu.ac.kr.aStartrip.dto.CardDTO;
import ce.daegu.ac.kr.aStartrip.dto.MemberDTO;
import ce.daegu.ac.kr.aStartrip.entity.Article;
import ce.daegu.ac.kr.aStartrip.entity.Card;
import ce.daegu.ac.kr.aStartrip.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
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
        List<Card> list = entity.getCardList();
        ArrayList<CardDTO> cList = new ArrayList<>();
        for (Card c : list) {
            CardDTO cc = CardDTO.builder()
                    .id(c.getId())
                    .articleNum(c.getArticle().getNum())
                    .cardType(c.getCardType())
                    .llmStatus(c.getLlmStatus())
                    .UserInput0(c.getUserInput0())
                    .LLMResponse0(c.getLLMResponse0())
                    .LLMResponse1(c.getLLMResponse1())
                    .LLMResponse2(c.getLLMResponse2()).build();
            cList.add(cc);
        }
        ArticleDTO dto = ArticleDTO.builder()
                .num(entity.getNum())
                .title(entity.getTitle())
                .writer(entity.getMember().getName())
                .content(entity.getContent())
                .hit(entity.getHit())
                .visibleBoard(entity.isVisibleBoard())
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .cardDTOList(cList)
                .build();
        return dto;
    }
}
