package ce.daegu.ac.kr.aStartrip.controller;

import ce.daegu.ac.kr.aStartrip.dto.MemberDTO;
import ce.daegu.ac.kr.aStartrip.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
public class LoginController {

    @Autowired
    private MemberService memberService;

    @GetMapping("/login")
    public String loginID() {
        log.info("loginID page");
        return "loginID";
    }

    @PostMapping("/login")
    public String login(@Validated MemberDTO dto, Model model) {
        log.info(dto.getEmail());
        model.addAttribute("email", dto.getEmail());
        if (memberService.findID(dto.getEmail())) {
            return "login";
        } else {
            return "regist";
        }
    }
}