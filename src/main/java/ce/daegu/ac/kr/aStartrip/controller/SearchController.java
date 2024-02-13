package ce.daegu.ac.kr.aStartrip.controller;

import ce.daegu.ac.kr.aStartrip.dto.ArticleDTO;
import ce.daegu.ac.kr.aStartrip.dto.MemberDetails;
import ce.daegu.ac.kr.aStartrip.service.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class SearchController {
    private final ArticleService articleService;

    @GetMapping("/search")
    public String search(Model model, @AuthenticationPrincipal MemberDetails memberDetails) {
        if (memberDetails != null) { //로그인 한 경우에만 유저이름 표기
            String userEmail = memberDetails.getUsername();
            String userName = memberDetails.getMember().getEmail();
            model.addAttribute("userEmail", userEmail);
            model.addAttribute("userName", userName);
        }
        return "search";
    }

    @GetMapping("/search/{input}")
    public String searchRedirect(@PathVariable("input") String input) throws UnsupportedEncodingException {
        // 원래 URL 부분에서 한글이 포함된 부분을 인코딩
        String encoded_input = URLEncoder.encode(input, "utf-8");
        return "redirect:/search/" + encoded_input + "/0";
    }

    @GetMapping("/search/{input}/{page}")
    public String doSearch(@PathVariable(name = "input") String input,
                           @PathVariable(name = "page") long page,
                           @AuthenticationPrincipal MemberDetails memberDetails, Model model) {

        if (input.equals("board")) {
            List<ArticleDTO> articleDTOList = articleService.getBoardArticleList((int) page, 6);
            model.addAttribute("articles", articleDTOList);
        } else {
            List<ArticleDTO> articleDTOList = articleService.getBoardArticleSearch(input, (int) page, 6);
            model.addAttribute("articles", articleDTOList);
        }
        log.debug("search: {} {}", input, page);

        if (memberDetails != null) { //로그인 한 경우에만 유저이름 표기
            String userEmail = memberDetails.getUsername();
            String userName = memberDetails.getMember().getEmail();
            model.addAttribute("userEmail", userEmail);
            model.addAttribute("userName", userName);
        }

        model.addAttribute("inputQuery", input);
        model.addAttribute("page", page);

        return "search";
    }
}
