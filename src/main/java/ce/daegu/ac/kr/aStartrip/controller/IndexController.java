package ce.daegu.ac.kr.aStartrip.controller;

import ce.daegu.ac.kr.aStartrip.dto.MemberDTO;
import lombok.extern.slf4j.Slf4j;
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
    public String index() {
        log.debug("index()");
        return "index";
    }

    @GetMapping("/login")
    public String loginAttempt() {
        log.info("loginID page");
        return "loginID";
    }

    @PostMapping("/login")
    public String loginAttempt(MemberDTO dto, RedirectAttributes redirectAttributes) {
        log.info(dto.getID());
        redirectAttributes.addFlashAttribute("ID", dto.getID());
        return "redirect:/login";
    }

    @GetMapping("/doLogin")
    public String login(){
        log.info("PW input");
        return "login";
    }
}
