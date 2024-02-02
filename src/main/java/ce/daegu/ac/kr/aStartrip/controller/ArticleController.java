package ce.daegu.ac.kr.aStartrip.controller;

import ce.daegu.ac.kr.aStartrip.dto.ArticleDTO;
import ce.daegu.ac.kr.aStartrip.dto.MemberDetails;
import ce.daegu.ac.kr.aStartrip.entity.Member;
import ce.daegu.ac.kr.aStartrip.repository.MemberRepository;
import ce.daegu.ac.kr.aStartrip.service.ArticleService;
import ce.daegu.ac.kr.aStartrip.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;

import ce.daegu.ac.kr.aStartrip.dto.MemberDTO;

import java.net.URLEncoder;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ArticleController {

    private final MemberService memberService;
    private final ArticleService articleService;
    private final MemberRepository memberRepository;

    @SneakyThrows
    @GetMapping("/article")
    public String article(@AuthenticationPrincipal MemberDetails memberDetails, Model Model) { // 새로운 학습 활동 시작
        Member member = null;
        String errorMsg = null;
        try {
            member = memberDetails.getMember();
            if (!member.getEmail().isBlank() && !member.getEmail().equals("")) {
                //member email가 비어있지 않고, '' 가 아닐때만
                long articleNum = articleService.addArticle(member);
                return "redirect:/article/" + articleNum;
            }
        } catch (NullPointerException nullPointerException) {
            errorMsg = "로그인이 필요합니다.";
        }

        errorMsg = URLEncoder.encode(errorMsg, "utf-8");
        return "redirect:/loginError?msg=" + errorMsg;
    }

    @SneakyThrows
    @GetMapping("/article/{num}")
    public String articleDetail(@PathVariable("num") String articleNum, Model model, @AuthenticationPrincipal MemberDetails memberDetails) {
        ArticleDTO articleDTO = articleService.findArticlebyID(Long.parseLong(articleNum));
        String memberName = null;
        String errorMsg = null;
        if (memberDetails != null) { //로그인 한 경우에만 유저이름 표기
            String userEmail = memberDetails.getUsername();
            String userName = memberDetails.getMember().getEmail();
            model.addAttribute("userEmail", userEmail);
            model.addAttribute("userName", userName);
        }

        try {
            //게시글에 접근 권한 있는지 넣어야함.
            memberName = memberDetails.getMember().getName();
        } catch (NullPointerException nullPointerException) {
            //비로그인 사용자 or 접근 권한 없는 사용자
            errorMsg = "로그인이 필요합니다.";
        }
        if (articleDTO.isVisibleBoard() || articleDTO.getWriter().equals(memberName)) {
            //게시글의 member와 요청온 member가 같은 경우에만 정상 리턴
            //or 게시글이 공개 상태인 경우 리턴
            model.addAttribute("articleNum", articleNum);
            model.addAttribute("article", articleDTO);
            articleService.viewCountAdd(Long.parseLong(articleNum));
            return "article/article";
        } else {
            errorMsg = "권한이 없는 게시글입니다.";
        }
        errorMsg = URLEncoder.encode(errorMsg, "utf-8");
        return "redirect:/loginError?msg=" + errorMsg;
    }
}
