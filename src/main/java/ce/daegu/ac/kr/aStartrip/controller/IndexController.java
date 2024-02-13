package ce.daegu.ac.kr.aStartrip.controller;

import ce.daegu.ac.kr.aStartrip.dto.ArticleDTO;
import ce.daegu.ac.kr.aStartrip.dto.MemberDTO;
import ce.daegu.ac.kr.aStartrip.dto.MemberDetails;
import ce.daegu.ac.kr.aStartrip.entity.Member;
import ce.daegu.ac.kr.aStartrip.service.ArticleService;
import ce.daegu.ac.kr.aStartrip.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
public class IndexController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private MemberService memberService;

    @GetMapping("/")
    public String index(Model model, Authentication authentication) {
        List<ArticleDTO> articleDTOList = articleService.getBoardArticleList(0, 10);
        model.addAttribute("articles", articleDTOList);

        if (authentication == null) { //로그인 안한 경우 model 빈 상태로 바로 인덱스 페이지 호출
            return "index";
        }

        MemberDetails memberDetails = (MemberDetails) authentication.getPrincipal();
        String userEmail = memberDetails.getUsername();
        String userName = memberDetails.getMember().getEmail();
        model.addAttribute("userEmail", userEmail);
        model.addAttribute("userName", userName);

        List<ArticleDTO> articleUserList = memberService.userArticleList(userEmail);
        model.addAttribute("userArticles", articleUserList);

        return "index";
    }
}
