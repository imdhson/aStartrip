package ce.daegu.ac.kr.aStartrip.service;

import ce.daegu.ac.kr.aStartrip.dto.ArticleDTO;
import ce.daegu.ac.kr.aStartrip.dto.CardDTO;
import ce.daegu.ac.kr.aStartrip.dto.MemberDTO;
import ce.daegu.ac.kr.aStartrip.entity.Member;
import ce.daegu.ac.kr.aStartrip.entity.Provider;
import ce.daegu.ac.kr.aStartrip.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
@Transactional
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

            Optional<Member> e = memberRepository.findById(toEmail);
            if(e.isPresent()) {
                Member m = e.get();
                m.setAuthCode(authCode);
                m.setActivation(false);

                memberRepository.save(m);
            }else{
                Member m = Member.builder()
                        .email(toEmail)
                        .authCode(authCode)
                        .activation(false).build();

                memberRepository.save(m);
            }

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

    @Override
    public boolean changePassword(MemberDTO dto) {
        Optional<Member> e = memberRepository.findById(dto.getEmail());
        // 이미 이메일 인증에서 확인하기에 다시 확인할 필요 X
        Member entity = e.get(); // 메일 인증번호를 가지고 있다.
        if(entity.getName().equals(dto.getName()) && entity.getBirthDate().equals(dto.getBirthDate()) &&
            entity.getTel().equals(dto.getTel())) {
            entity.setPW(passwordEncoder.encode(dto.getPW()));
            entity.setActivation(true);
            entity.setAuthCode(dto.getAuthCode()); // dto는 인증번호를 가지고 있지 않다.
            memberRepository.save(entity);
            return true;
        }
        return false;
    }

    private boolean checkUsingEmail(String email) {
        Optional<Member> member = memberRepository.findById(email);
        if (member.isPresent() && member.get().isActivation() && member.get().getAuthCode() != null) {
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
    public Member register(MemberDTO memberDTO, Provider provider) {
        memberDTO.setPW(passwordEncoder.encode(memberDTO.getPW()));
        memberDTO.setActivation(true);
        Member entity = dtoToEntity(memberDTO);
        entity.setProvider(provider);
        entity = memberRepository.save(entity);
        if (entity.getEmail().isEmpty() || entity.getEmail() == null) {
            return null;
        }
        return entity;
    }
}
