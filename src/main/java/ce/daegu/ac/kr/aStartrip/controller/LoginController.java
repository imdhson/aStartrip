package ce.daegu.ac.kr.aStartrip.controller;

import ce.daegu.ac.kr.aStartrip.dto.MemberDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
public class LoginController {

    @GetMapping("/login")
    public String loginAttempt() {
        log.info("loginID page");
        return "loginID";
    }

    @PostMapping("/login")
    public String loginAttempt(MemberDTO dto, Model model) {
        log.info(dto.getID());
        model.addAttribute("ID", dto.getID());
        return "login";
    }
}
