package ce.daegu.ac.kr.aStartrip.controller;

import ce.daegu.ac.kr.aStartrip.dto.MemberDTO;
import ce.daegu.ac.kr.aStartrip.dto.MemberDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
public class IndexController {
    @GetMapping("/")
    public String index(Model model, Authentication authentication) {
        if (authentication == null){
            return "index";
        }
        MemberDetails memberDetails = (MemberDetails) authentication.getPrincipal();
        log.debug("index()" + memberDetails.getUsername());
        model.addAttribute("username", memberDetails.getUsername());
        return "index";
    }
}
