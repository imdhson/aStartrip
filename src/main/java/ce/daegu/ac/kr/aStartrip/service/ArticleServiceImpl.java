package ce.daegu.ac.kr.aStartrip.service;

import ce.daegu.ac.kr.aStartrip.dto.ArticleDTO;
import ce.daegu.ac.kr.aStartrip.dto.CardDTO;
import ce.daegu.ac.kr.aStartrip.dto.MemberDTO;
import ce.daegu.ac.kr.aStartrip.entity.Article;
import ce.daegu.ac.kr.aStartrip.entity.ArticlePermissionENUM;
import ce.daegu.ac.kr.aStartrip.entity.Card;
import ce.daegu.ac.kr.aStartrip.entity.Member;
import ce.daegu.ac.kr.aStartrip.repository.ArticleRepository;
import ce.daegu.ac.kr.aStartrip.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final CardService cardService;
    private final CardRepository cardRepository;
    private final FileService fileService;

    @Override
    public List<ArticleDTO> getBoardArticleList(int page, int size) {
        List<ArticleDTO> articleDTOList = new ArrayList<>();
//        List<Article> entities = articleRepository.findAll();
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Article> entities = articleRepository.findByArticlePermissionOrderByHitDesc(ArticlePermissionENUM.OPEN, pageRequest);
        for (Article entity : entities) {
            ArticleDTO dto = entityToDto(entity);
            articleDTOList.add(dto);
        }
        return articleDTOList;
    }

    @Override
    public List<ArticleDTO> getBoardArticleSearch(String input, int page, int size) {
        List<ArticleDTO> articleDTOList = new ArrayList<>();
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Article> articles = articleRepository.findByTitleContainingOrMemberEmailContaining(input, pageRequest);
        for (Article entity: articles){
            ArticleDTO articleDTO = entityToDto(entity);
            articleDTOList.add(articleDTO);
        }
        return articleDTOList;
    }


    @Override
    public long addArticle(Member member) {
        Article article = Article.builder()
                .hit(0).articlePermission(ArticlePermissionENUM.ONLYME).member(member).build();
        article = articleRepository.save(article);
        return article.getNum();
    }

    @Override
    public boolean updateArticle(String email, ArticleDTO articleDTO) {
        Optional<Article> entity = articleRepository.findById(articleDTO.getNum());
        if (entity.isPresent()) {
            Article article = entity.get();

            if (email.equals(article.getMember().getEmail())) {
                if (articleDTO.getTitle() != null) {
                    article.setTitle(articleDTO.getTitle());
                } else if (articleDTO.getArticlePermission() != null) {
                    article.setArticlePermission(articleDTO.getArticlePermission());
                }
                log.info("updateArticle" + article.toString());
                articleRepository.save(article);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean updateCard1(String username, long articleId, CardDTO cardDTO, long key) {
        Optional<Article> e = articleRepository.findById(articleId);
        if (username.equals(e.get().getMember().getEmail())) {
            return cardService.updateCard2(cardDTO, key);
        }
        return false;
    }

    @Override
    public boolean deleteArticle(long articleNum, Member member) {
        Optional<Article> articleOptional = articleRepository.findById(articleNum);
        if (articleOptional.isPresent()) {
            Article article = articleOptional.get();
            if (article.getMember().getEmail().equals(member.getEmail())) {

                ArrayList<Card> articleCardList = new ArrayList<>(article.getCardList());
                articleCardList.forEach(card_e -> { //카드리스트 순회하며 파일 제거
                    log.debug("card 삭제 중 : {}", card_e.toString());
                    fileService.deleteFile(card_e.getId());
                });

                article.setCardList(new ArrayList<Card>()); //카드리스트 비우기 (외래키 연결 제거)
                cardRepository.deleteAll(articleCardList); //카드리스트를 대입하여 거기 들어간 카드들을 제거하도록 함.

                articleRepository.delete(article); //게시글 제거
                return true;
            }
        }
        return false;
    }

    @Override
    public ArticleDTO findArticlebyID(long articleNum) {
        Optional<Article> articleOptional = articleRepository.findById(articleNum);
        if (articleOptional.isPresent()) {
            return entityToDto(articleOptional.get());
        } else {
            return null;
        }
    }

    @Override
    public void viewCountAdd(long articleNum) {
        if (articleRepository.findById(articleNum).isPresent()) {
            Article article = articleRepository.findById(articleNum).get();
            article.setHit(article.getHit() + 1);
            articleRepository.save(article);
        }
    }

}
