package ce.daegu.ac.kr.aStartrip.controller;

import ce.daegu.ac.kr.aStartrip.dto.MemberDTO;
import ce.daegu.ac.kr.aStartrip.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MailController {

    private final MemberService memberService;

    @GetMapping("/user/email")
    public String findEmailForm() {
        return "emailForm";
    }

    @PostMapping("/user/email")
    public String findEmail(MemberDTO dto, Model model) {
        log.info("MemberDTO : " + dto.toString());
        // 이름, 생년월일, 전화번호를 기반으로 기본키(email)을 찾는 코드를 작성해야 한다.
        return "login";
    }
    // 위 코드 삭제 예정-------
    @PostMapping("/email/verification-requests")
    public ResponseEntity sendMessage(@RequestBody MemberDTO dto, Model model) {
        HttpHeaders httpheaders = new HttpHeaders();
        httpheaders.setContentType(MediaType.APPLICATION_JSON);
        log.info("this is email : " + dto.getEmail());

        boolean result = memberService.sendCodeToEmail(dto.getEmail());

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping("/email/verifications")
    public ResponseEntity verificationEmail(@RequestBody MemberDTO dto, Model model) {
        Map<String, Boolean> map = new HashMap<>();
        boolean response = memberService.verifiedCode(dto.getEmail(), dto.getAuthCode());

        map.put("confirm", response);

        return ResponseEntity.status(HttpStatus.OK).body(map);
    }
}
