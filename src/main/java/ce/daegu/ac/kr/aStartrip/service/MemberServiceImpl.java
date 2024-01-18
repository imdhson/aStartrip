package ce.daegu.ac.kr.aStartrip.service;

import ce.daegu.ac.kr.aStartrip.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    @Override
    public boolean findID(String email) {
        if(memberRepository.findById(email).isEmpty()){
            return false;
        }
        return true;
    }
}
