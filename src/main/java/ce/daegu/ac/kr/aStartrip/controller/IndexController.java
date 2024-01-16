package ce.daegu.ac.kr.aStartrip.controller;

import ce.daegu.ac.kr.aStartrip.dto.MemberDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
public class IndexController {
    @GetMapping("/")
    public String index(Model model) {
        log.debug("index()");
        model.addAttribute("name", "test");
        return "index";
    }

    @GetMapping("/signup")
    public String signup() {
        log.info("sign up page");
        return "signup";
    }

    @PostMapping("/signup")
    public String regist(MemberDTO dto, RedirectAttributes redirectAttributes) {
        log.info(dto.toString());
        return "redirect:/";
    }
}
