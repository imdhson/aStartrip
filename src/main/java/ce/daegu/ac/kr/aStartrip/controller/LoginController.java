package ce.daegu.ac.kr.aStartrip.controller;

import ce.daegu.ac.kr.aStartrip.dto.MemberDTO;
import ce.daegu.ac.kr.aStartrip.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String login(MemberDTO dto, Model model) {
        // ID가 들어오면
        log.info(dto.getID());
        model.addAttribute("ID", dto.getID());
        if(memberService.findID(dto.getID())){
            return "login";
        }else{
            return "regist";
        }
    }
}