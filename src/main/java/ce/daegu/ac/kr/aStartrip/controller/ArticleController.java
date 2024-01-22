package ce.daegu.ac.kr.aStartrip.controller;

import ce.daegu.ac.kr.aStartrip.dto.MemberDetails;
import ce.daegu.ac.kr.aStartrip.entity.Member;
import ce.daegu.ac.kr.aStartrip.service.ArticleService;
import ce.daegu.ac.kr.aStartrip.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;

import ce.daegu.ac.kr.aStartrip.dto.MemberDTO;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ArticleController {

    private final MemberService memberService;
    private final ArticleService articleService;

    @GetMapping("/article")
    public String article(@AuthenticationPrincipal MemberDetails memberDetails, Model Model) { // 새로운 학습 활동 시작
        log.debug("article()");
        Member member = memberDetails.getMember();
        long articleNum = articleService.addArticle(member);
        return "redirect:/article/" + articleNum;
    }

    // reading -------

    @GetMapping("/article/r01")
    public String r01() {
        log.debug("r01()");
        return "article/r01";
    }

    @GetMapping("/article/r02")
    public String r02() {
        log.debug("r02()");
        return "article/r02";
    }

    // writing -------
    @GetMapping("/article/w01")
    public String w01() {
        log.debug("w01()");
        return "article/w01";
    }

    @GetMapping("/article/w02")
    public String w02() {
        log.debug("w02()");
        return "article/w02";
    }

    //vocabulary --------
    @GetMapping("/article/v01")
    public String v01() {
        log.debug("v01()");
        return "article/v01";
    }

    @GetMapping("/article/v02")
    public String v02() {
        log.debug("v02()");
        return "article/v02";
    }

    @GetMapping("/article/{num}")
    public String articleDetail(@PathVariable("num") String articleNum) {
        log.debug("articleDetail({})", articleNum);
        return "article/article";
    }
}
