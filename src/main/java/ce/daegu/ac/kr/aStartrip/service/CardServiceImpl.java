package ce.daegu.ac.kr.aStartrip.service;

import ce.daegu.ac.kr.aStartrip.dto.ArticleDTO;
import ce.daegu.ac.kr.aStartrip.dto.CardDTO;
import ce.daegu.ac.kr.aStartrip.entity.Article;
import ce.daegu.ac.kr.aStartrip.entity.Card;
import ce.daegu.ac.kr.aStartrip.entity.LLMStatusENUM;
import ce.daegu.ac.kr.aStartrip.entity.Member;
import ce.daegu.ac.kr.aStartrip.repository.ArticleRepository;
import ce.daegu.ac.kr.aStartrip.repository.CardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class CardServiceImpl implements CardService {
    private final ArticleRepository articleRepository;
    private final CardRepository cardRepository;
    private final LLMService llmService;
    private final FileService fileService;

    @Override
    public boolean addCard(long num, CardDTO cardDTO) {
        Optional<Article> articleOptional = articleRepository.findById(num);
        if (articleOptional.isPresent()) {
            Article article = articleOptional.get();
            Card card = dtoToEntity(cardDTO);
            card.setLlmStatus(LLMStatusENUM.NEW);
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
    public boolean delCard(long cardId, Member member) {
        Optional<Card> cardOptional = cardRepository.findById(cardId);
        Card card = null;
        if (cardOptional.isPresent()) {
            card = cardOptional.get();
        }

        Article article = card.getArticle();
        ArrayList<Card> articleCardList = new ArrayList<>(article.getCardList());
        articleCardList.remove(card);
        article.setCardList(articleCardList);

        if (member.getEmail().equals(card.getArticle().getMember().getEmail())) {
            //작성자인 경우만 삭제 수행
            log.debug("card 삭제 중 : {}", card.toString());
            cardRepository.delete(card);
            fileService.deleteFile(cardId);
            return true;
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
    public long getArticleId(CardDTO cardDTO) {
        Optional<Card> e = cardRepository.findById(cardDTO.getId());
        return e.get().getArticle().getNum();
    }

    @Override
    public boolean updateCard2(CardDTO cardDTO, long key) {
        Optional<Card> e = cardRepository.findById(cardDTO.getId());
        if (e.isPresent()) {
            Card entity = e.get();
            if (cardDTO.getUserInput0() != null && !cardDTO.getUserInput0().isEmpty()) {
                entity.setUserInput0(cardDTO.getUserInput0());
            }
            if (cardDTO.getLLMResponse0() != null && !cardDTO.getLLMResponse0().isEmpty()) {
                entity.setLLMResponse0(cardDTO.getLLMResponse0());
            }
            if (cardDTO.getLLMResponse1() != null && !cardDTO.getLLMResponse1().isEmpty()) {
                entity.setLLMResponse1(cardDTO.getLLMResponse1());
            }
            if (cardDTO.getLLMResponse2() != null && !cardDTO.getLLMResponse2().isEmpty()) {
                entity.setLLMResponse2(cardDTO.getLLMResponse2());
            }
            if (cardDTO.getLlmStatus() != null) {
                entity.setLlmStatus(cardDTO.getLlmStatus());
            }
            cardRepository.save(entity);
            if (entity.getLlmStatus() == LLMStatusENUM.GENERATING) {
                //LLM API 사용 시작
                CompletableFuture.runAsync(() -> { //비동기처리
                    boolean pass = llmService.execute(entity);
                    // pass가 false면 canceled로 변경
                    if (!pass) {
                        entity.setLlmStatus(LLMStatusENUM.CANCELED);
                    }
                    // 이곳에서 card ws 브로드 캐스트를 수행해야 함. -> true
                    Optional<Card> e1 = cardRepository.findById(cardDTO.getId());
                    CardDTO dto = entityToDto(e1.get());

                    llmService.completeWaiting(dto, key);
                });
            }
            return true;
        }
        return false;
    }

    @Override
    public CardDTO updateCard(CardDTO cardDTO) {
        //updateCard 수행 시 llmstatus 확인해서 generating 인 경우, LLMService 실행 bard 경유
        return null;
    }


}
