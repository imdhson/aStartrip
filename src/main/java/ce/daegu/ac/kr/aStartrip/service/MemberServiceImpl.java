package ce.daegu.ac.kr.aStartrip.service;

import ce.daegu.ac.kr.aStartrip.dto.ArticleDTO;
import ce.daegu.ac.kr.aStartrip.dto.CardDTO;
import ce.daegu.ac.kr.aStartrip.dto.MemberDTO;
import ce.daegu.ac.kr.aStartrip.entity.Member;
import ce.daegu.ac.kr.aStartrip.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    @Value("${spring.mail.auth-code-expiration-millis}")
    private long authCodeExpirationMillis;
    private static final String AUTH_CODE_PREFIX = "AuthCode ";//?
    private final MemberRepository memberRepository;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean findID(String email) {
        if (memberRepository.findById(email).isEmpty()) {
            return false;
        }
        return true;
    }

    @Override
    public MemberDTO findMemberById(String email) {
        if (memberRepository.findById(email).isPresent()) {
            Member member = memberRepository.findById(email).get();
            return entityToDto(member);
        }
        return null;
    }

    @Override
    public List<ArticleDTO> userArticleList(String email) {
        Optional<Member> m = memberRepository.findById(email);
        if (m.isEmpty()) {
            return null;
        }
        MemberDTO dto = entityToDto(m.get());
        return dto.getArticleDTOList();
    }

    @Override
    public boolean sendCodeToEmail(String toEmail) {
        if (this.checkUsingEmail(toEmail)) {
            String title = "AStartrip 서비스 인증 번호";
            String authCode = this.createCode();

            log.info("authCode : " + authCode);
            MemberDTO member = new MemberDTO();
            member.setEmail(toEmail);
            member.setAuthCode(authCode);
            member.setActivation(false);

            memberRepository.save(dtoToEntity(member));

            mailService.sendEmail(toEmail, title, authCode);

            return true;
            // 이메일 전송 후 인증번호는 DB에 저장하여 기록.
            // 새로운 인증번호를 받기 전까지 재사용하며,
            // 비밀번호 변경 등 개인정보 변경이 일어날 때마다 인증번호도 수정된다.
        } else{
            return false;
        }
    }

    @Override
    public boolean verifiedCode(String email, String authCode) {
        Optional<Member> member = memberRepository.findById(email);
        if (member.isEmpty()) {
            return false;
        }
        Member m = member.get();
        if (authCode.equals(m.getAuthCode())) {
            return true;
        }
        return false;
    }

    private boolean checkUsingEmail(String email) {
        Optional<Member> member = memberRepository.findById(email);
        if (member.isPresent() && member.get().isActivation()) {
            log.debug("MemberServiceImpl.checkUsingEmail exception occur email: {}", email);
            return false;
        }
        return true;
    }

    private String createCode() {
        String str = "";
        for (int i = 0; i < 6; i++) {
            int a = (int) (Math.random() * 10);
            str = str + a;
        }
        return str;
    }


    @Override
    public boolean register(MemberDTO memberDTO) {
        memberDTO.setPW(passwordEncoder.encode(memberDTO.getPW()));
        memberDTO.setActivation(true);
        Member entity = dtoToEntity(memberDTO);
        entity = memberRepository.save(entity);
        if (entity.getEmail().isEmpty() || entity.getEmail() == null) {
            return false;
        }
        return true;


    }
}
