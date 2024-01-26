package ce.daegu.ac.kr.aStartrip.service;

import ce.daegu.ac.kr.aStartrip.dto.ArticleDTO;
import ce.daegu.ac.kr.aStartrip.dto.CardDTO;
import ce.daegu.ac.kr.aStartrip.dto.MemberDTO;
import ce.daegu.ac.kr.aStartrip.entity.Article;
import ce.daegu.ac.kr.aStartrip.entity.Card;
import ce.daegu.ac.kr.aStartrip.entity.Member;
import ce.daegu.ac.kr.aStartrip.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final  CardService cardService;

    @Override
    public List<ArticleDTO> getAllArticleList() {
        List<ArticleDTO> articleDTOList = new ArrayList<>();
        List<Article> entities = articleRepository.findAll();
        for (Article entity : entities) {
            ArticleDTO dto = entityToDto(entity);
            articleDTOList.add(dto);
        }
        return articleDTOList;
    }

    @Override
    public long addArticle(Member member) {
        Article article = Article.builder()
                .hit(0).visibleBoard(false).member(member).build();
        article = articleRepository.save(article);
        return article.getNum();
    }

    @Override
    public boolean updateArticle(String email, ArticleDTO articleDTO) {
        Optional<Article> entity = articleRepository.findById(articleDTO.getNum());
        if(entity.isPresent()) {
            Article article = entity.get();

            if (email.equals(article.getMember().getEmail())) {
                article.setTitle(articleDTO.getTitle());
                log.info("@@@@@@@@@@@@@@@@@" + article.toString());
                articleRepository.save(article);
            }
            return true;
        }
        return false;
    }

    @Override
    public void updateCard1(String username, long articleId, CardDTO cardDTO) {
        Optional<Article> e = articleRepository.findById(articleId);
        if(username.equals(e.get().getMember().getEmail())) {
            cardService.updateCard2(cardDTO);
        }
    }

}
