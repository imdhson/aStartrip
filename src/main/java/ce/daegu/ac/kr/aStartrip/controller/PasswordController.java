package ce.daegu.ac.kr.aStartrip.controller;

import ce.daegu.ac.kr.aStartrip.dto.MemberDTO;
import ce.daegu.ac.kr.aStartrip.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
public class PasswordController {

    @Autowired
    private MemberService memberService;

    @GetMapping("/user/password")
    public String updatePasswordForm() {
        return "updatePassword";
    }

    @PostMapping("/user/password")
    public ResponseEntity updatePassword(MemberDTO dto, Model model) {


        return ResponseEntity.status(HttpStatus.OK).body(true);
    }
}
