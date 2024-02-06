package ce.daegu.ac.kr.aStartrip.controller;

import ce.daegu.ac.kr.aStartrip.dto.ArticleDTO;
import ce.daegu.ac.kr.aStartrip.dto.CardDTO;
import ce.daegu.ac.kr.aStartrip.dto.MemberDTO;
import ce.daegu.ac.kr.aStartrip.dto.MemberDetails;
import ce.daegu.ac.kr.aStartrip.entity.Article;
import ce.daegu.ac.kr.aStartrip.entity.ArticlePermissionENUM;
import ce.daegu.ac.kr.aStartrip.entity.Member;
import ce.daegu.ac.kr.aStartrip.repository.ArticleRepository;
import ce.daegu.ac.kr.aStartrip.repository.MemberRepository;
import ce.daegu.ac.kr.aStartrip.service.ArticleService;
import ce.daegu.ac.kr.aStartrip.service.CardService;
import ce.daegu.ac.kr.aStartrip.service.MemberService;
import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ArticleRController {
    private final ArticleService articleService;
    private final CardService cardService;
    private final MemberService memberService; // !
    private final MemberRepository memberRepository; // !

    @GetMapping("/api/article/{num}")
    public ResponseEntity<ArticleDTO> articleDetail(@PathVariable("num") long num, @AuthenticationPrincipal MemberDetails memberDetails) {
        HttpHeaders httpheaders = new HttpHeaders();
        httpheaders.setContentType(MediaType.APPLICATION_JSON);
        String memberName = null;
        try {
            memberName = memberDetails.getMember().getName();
        } catch (NullPointerException nullPointerException) {
            //비로그인 사용자 or 접근 권한 없는 사용자
        }

        //게시글 가져오기
        ArticleDTO articleDTO = articleService.findArticlebyID(num);
        if (articleDTO.getWriter().equals(memberName) || articleDTO.getArticlePermission() == ArticlePermissionENUM.OPEN) {
            //게시글의 member와 요청온 member가 같은 경우에만 정상 리턴
            //or visibleBoard가 true면 리턴
            return ResponseEntity.status(HttpStatus.OK).headers(httpheaders).body(articleDTO);
        }
        return ResponseEntity.badRequest().headers(httpheaders).build();
    }

    @DeleteMapping("/api/article/{num}")
    public ResponseEntity<String> deleteArticle(@PathVariable("num") long articleNum, @AuthenticationPrincipal MemberDetails memberDetails) {
        Member member;
        try {
            member = memberDetails.getMember();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        if (articleService.deleteArticle(articleNum, memberDetails.getMember())) {
            return ResponseEntity.ok().body("true");
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/api/card/{num}")
    public ResponseEntity<String> deleteCard(@PathVariable("num") long cardId, @AuthenticationPrincipal MemberDetails memberDetails) {
        return ResponseEntity.ok().body("true");
    }
}
