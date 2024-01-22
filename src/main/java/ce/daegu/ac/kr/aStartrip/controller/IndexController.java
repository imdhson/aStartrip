package ce.daegu.ac.kr.aStartrip.controller;

import ce.daegu.ac.kr.aStartrip.dto.ArticleDTO;
import ce.daegu.ac.kr.aStartrip.dto.MemberDTO;
import ce.daegu.ac.kr.aStartrip.dto.MemberDetails;
import ce.daegu.ac.kr.aStartrip.service.ArticleService;
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

    @GetMapping("/")
    public String index(Model model, Authentication authentication) {
        List<ArticleDTO> articleDTOList = articleService.getAllArticleList();
        model.addAttribute("articles", articleDTOList);

        if (authentication == null){
            return "index";
        }
        MemberDetails memberDetails = (MemberDetails) authentication.getPrincipal();
        log.debug("index()" + memberDetails.getUsername());
        model.addAttribute("username", memberDetails.getUsername());
        return "index";
    }
}
