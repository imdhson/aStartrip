package ce.daegu.ac.kr.aStartrip.controller;

import ce.daegu.ac.kr.aStartrip.dto.ArticleDTO;
import ce.daegu.ac.kr.aStartrip.dto.MemberDetails;
import ce.daegu.ac.kr.aStartrip.entity.Article;
import ce.daegu.ac.kr.aStartrip.repository.ArticleRepository;
import ce.daegu.ac.kr.aStartrip.service.ArticleService;
import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ArticleRController {
    private final ArticleRepository articleRepository;
    private final ArticleService articleService;

    @GetMapping("/api/article/{num}")
    public ResponseEntity<ArticleDTO> articleDetail(@PathVariable("num") long num, @AuthenticationPrincipal MemberDetails memberDetails) {
        HttpHeaders httpheaders = new HttpHeaders();
        httpheaders.setContentType(MediaType.APPLICATION_JSON);

        try{
            //게시글에 접근 권한 있는지 넣어야함.
            memberDetails.getMember().getName();
        } catch (NullPointerException nullPointerException){
            //비로그인 사용자 or 접근 권한 없는 사용자
            return ResponseEntity.badRequest().headers(httpheaders).build();
        }
        log.debug("00000000000", memberDetails.getMember().getName());
        log.debug("00000000000   : "+ num);
        Optional<Article> articleOptional = articleRepository.findById(num);
        if (articleOptional.isPresent()) {
            Article article = articleOptional.get();
            ArticleDTO articleDTO = articleService.entityToDto(article);
            log.debug("toSTRING111111111"+articleDTO.toString());

            return ResponseEntity.status(HttpStatus.OK).headers(httpheaders).body(articleDTO);
        }
        return ResponseEntity.badRequest().headers(httpheaders).build();
    }
}
