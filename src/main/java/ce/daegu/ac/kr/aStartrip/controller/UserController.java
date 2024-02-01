package ce.daegu.ac.kr.aStartrip.controller;

import ce.daegu.ac.kr.aStartrip.dto.MemberDTO;
import ce.daegu.ac.kr.aStartrip.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final MemberService memberService;

    @GetMapping("/login")
    public String loginID() {
        log.info("loginID page");
        return "loginID";
    }

    @PostMapping("/login")
    public String login(@Validated MemberDTO dto, Model model) {
        log.info("email: {}", dto.getEmail());
        model.addAttribute("email", dto.getEmail());
        if (memberService.findID(dto.getEmail())) {
            return "login";
        } else {
            return "regist";
        }
    }

    @GetMapping("/regist")
    public String regist() {
        log.info("regist form");
        return "regist";
    }

    @PostMapping("/regist")
    public String doRegist(MemberDTO dto, Model model) {
        log.info(dto.toString());
        if (memberService.register(dto)) {
            return "redirect:/";
        } else {
            model.addAttribute("email", dto.getEmail());
            return "regist";
        }
    }
}