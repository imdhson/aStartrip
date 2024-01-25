package ce.daegu.ac.kr.aStartrip.service;

import ce.daegu.ac.kr.aStartrip.dto.CardDTO;
import ce.daegu.ac.kr.aStartrip.entity.Article;
import ce.daegu.ac.kr.aStartrip.entity.Card;
import ce.daegu.ac.kr.aStartrip.repository.ArticleRepository;
import ce.daegu.ac.kr.aStartrip.repository.CardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class CardServiceImpl implements CardService {
    private final ArticleRepository articleRepository;
    private final CardRepository cardRepository;

    @Override
    public boolean addCard(long num, CardDTO cardDTO) {
        Optional<Article> articleOptional = articleRepository.findById(num);
        if (articleOptional.isPresent()) {
            Article article = articleOptional.get();
            Card card = dtoToEntity(cardDTO);
            card.setArticle(article);
            Card card1 = cardRepository.save(card);
            log.debug("Debug card1::::::::{}", card1.toString());
            log.debug("Debug getID::::::::{}", card1.getId());
            if (card1.getId() != null && card1.getId() >= 0L) {
                return true;
            }
        }
        return false;
    }

    @Override
    public CardDTO findCardById(long id) {
        Optional<Card> cardOptional = cardRepository.findById(id);
        if (cardOptional.isPresent()) {
            return entityToDto(cardOptional.get());
        }
        return null;
    }

    @Override
    public CardDTO updateCard(CardDTO cardDTO) {
        //updateCard 수행 시 llmstatus 확인해서 generating 인 경우, LLMService 실행
        return null;
    }


}
