package ce.daegu.ac.kr.aStartrip.service;

import ce.daegu.ac.kr.aStartrip.dto.ArticleDTO;
import ce.daegu.ac.kr.aStartrip.dto.CardDTO;
import ce.daegu.ac.kr.aStartrip.entity.Article;
import ce.daegu.ac.kr.aStartrip.entity.Card;
import ce.daegu.ac.kr.aStartrip.entity.Member;

public interface CardService {
    boolean addCard(long articleNum, CardDTO cardDTO);
    boolean delCard(long cardId, Member member);
    CardDTO updateCard(CardDTO cardDTO);
    CardDTO findCardById(long id);

    long getArticleId(CardDTO cardDTO);

    boolean updateCard2(CardDTO cardDTO, long key);

    default Card dtoToEntity(CardDTO cardDTO) {
        Card card = Card.builder()
                .id(cardDTO.getId())
                .cardType(cardDTO.getCardType())
                .llmStatus(cardDTO.getLlmStatus())
                .UserInput0(cardDTO.getUserInput0())
                .LLMResponse0(cardDTO.getLLMResponse0())
                .LLMResponse1(cardDTO.getLLMResponse1())
                .LLMResponse2(cardDTO.getLLMResponse2())
                .build();
        return card;
    }

    default CardDTO entityToDto(Card entity) {
        CardDTO cardDTO = CardDTO.builder()
                .id(entity.getId())
                .articleNum(entity.getArticle().getNum())
                .cardType(entity.getCardType())
                .llmStatus(entity.getLlmStatus())
                .UserInput0(entity.getUserInput0())
                .LLMResponse0(entity.getLLMResponse0())
                .LLMResponse1(entity.getLLMResponse1())
                .LLMResponse2(entity.getLLMResponse2())
                .build();
        return cardDTO;
    }
}
