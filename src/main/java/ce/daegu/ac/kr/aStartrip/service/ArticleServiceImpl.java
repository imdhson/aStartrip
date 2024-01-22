package ce.daegu.ac.kr.aStartrip.service;

import ce.daegu.ac.kr.aStartrip.dto.ArticleDTO;
import ce.daegu.ac.kr.aStartrip.dto.MemberDTO;
import ce.daegu.ac.kr.aStartrip.entity.Article;
import ce.daegu.ac.kr.aStartrip.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ArticleServiceImpl implements ArticleService{

    @Autowired
    private ArticleRepository articleRepository;

    @Override
    public List<ArticleDTO> getAllArticleList() {
        List<ArticleDTO> articleDTOList = new ArrayList<>();
        List<Article> entities = articleRepository.findAll();
        for(Article entity : entities){
            ArticleDTO dto = entityToDto(entity);
            articleDTOList.add(dto);
        }
        return articleDTOList;
    }

    @Override
    public long AddArticle(MemberDTO memberDTO) {
        return 0;
    }
}
