package ce.daegu.ac.kr.aStartrip.controller;

import ce.daegu.ac.kr.aStartrip.dto.MemberDTO;
import ce.daegu.ac.kr.aStartrip.dto.MemberDetails;
import ce.daegu.ac.kr.aStartrip.entity.Member;
import ce.daegu.ac.kr.aStartrip.service.MemberService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserController {
    private final MemberService memberService;

    @GetMapping("/login")
    public String loginID(@AuthenticationPrincipal MemberDetails memberDetails, Model model) {
        if (memberDetails != null) { //로그인 한 경우에만 유저이름 표기
            String userEmail = memberDetails.getUsername();
            String userName = memberDetails.getMember().getEmail();
            model.addAttribute("userEmail", userEmail);
            model.addAttribute("userName", userName);
        }
        return "loginID";
    }

    @PostMapping("/login")
    public String login(@Validated MemberDTO dto, Model model, @AuthenticationPrincipal MemberDetails memberDetails) {
        log.info("email: {}", dto.getEmail());
        model.addAttribute("email", dto.getEmail());
        if (memberDetails != null) { //로그인 한 경우에만 유저이름 표기
            String userEmail = memberDetails.getUsername();
            String userName = memberDetails.getMember().getEmail();
            model.addAttribute("userEmail", userEmail);
            model.addAttribute("userName", userName);
        }
        if (memberService.findID(dto.getEmail()) && memberService.findMemberById(dto.getEmail()).isActivation()) {
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
    public String doRegist(MemberDTO dto, Model model, @AuthenticationPrincipal MemberDetails memberDetails) {
        if (memberDetails != null) { //로그인 한 경우에만 유저이름 표기
            String userEmail = memberDetails.getUsername();
            String userName = memberDetails.getMember().getEmail();
            model.addAttribute("userEmail", userEmail);
            model.addAttribute("userName", userName);
        }
        Member member = memberService.register(dto); //회원가입 수행
        if (member != null) {
            //수행완료
            return "redirect:/login";
        } else {
            model.addAttribute("email", dto.getEmail());
            return "regist";
        }
    }

    @GetMapping("/user/password")
    public String updatePasswordForm() {
        return "updatePassword";
    }

    @PostMapping("/user/password")
    public ResponseEntity<Object> updatePassword(@RequestBody MemberDTO dto) {
        return ResponseEntity.status(HttpStatus.OK).body(true);
    }

    @GetMapping("/my")
    public String my(@AuthenticationPrincipal MemberDetails memberDetails, Model model) {
        Member member = memberDetails.getMember();
        MemberDTO memberDTO = MemberDTO.builder()
                .name(member.getName())
                .email(member.getEmail())
                .tel(member.getTel())
                .activation(member.isActivation())
                .birthDate(member.getBirthDate())
                .modDate(member.getModDate())
                .regDate(member.getRegDate()).build();

        if (memberDetails != null) { //로그인 한 경우에만 유저이름 표기
            String userEmail = memberDetails.getUsername();
            String userName = memberDetails.getMember().getEmail();
            model.addAttribute("userEmail", userEmail);
            model.addAttribute("userName", userName);
        }

        model.addAttribute("member", memberDTO);
        return "/my";
    }
}

