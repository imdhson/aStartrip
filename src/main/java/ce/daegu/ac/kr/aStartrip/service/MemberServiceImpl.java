package ce.daegu.ac.kr.aStartrip.service;

import ce.daegu.ac.kr.aStartrip.dto.MemberDTO;
import ce.daegu.ac.kr.aStartrip.entity.Member;
import ce.daegu.ac.kr.aStartrip.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean findID(String email) {
        if (memberRepository.findById(email).isEmpty()) {
            return false;
        }
        return true;
    }

    public boolean register(MemberDTO memberDTO) {
        memberDTO.setPW(passwordEncoder.encode(memberDTO.getPW()));
        Member entity = dtoToEntity(memberDTO);
        entity = memberRepository.save(entity);
        if (entity.getEmail().isEmpty() || entity.getEmail() == null) {
            return false;
        }
        return true;


    }
}
