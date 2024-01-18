package ce.daegu.ac.kr.aStartrip.controller;

import ce.daegu.ac.kr.aStartrip.dto.MemberDTO;
import ce.daegu.ac.kr.aStartrip.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
public class RegistController {

    @Autowired
    private MemberService memberService;

    @GetMapping("/regist")
    public String regist() {
        log.info("regist form");
        return "regist";
    }

    @PostMapping("/regist")
    public String doRegist(MemberDTO dto, Model model){
        log.info(dto.toString());
        if(memberService.register(dto)){
            return "/";
        }
        else{
            model.addAttribute("email", dto.getEmail());
            return "redirect:/regist";
        }
    }
}
