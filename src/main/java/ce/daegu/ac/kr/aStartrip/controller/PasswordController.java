package ce.daegu.ac.kr.aStartrip.controller;

import ce.daegu.ac.kr.aStartrip.dto.MemberDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@Controller
public class PasswordController {

    @GetMapping("/user/password")
    public String updatePasswordForm() {
        return "updatePassword";
    }

    @PostMapping("/user/password")
    public ResponseEntity<Object> updatePassword(@RequestBody MemberDTO dto) {
        return ResponseEntity.ok(null);
    }
}
