package ce.daegu.ac.kr.aStartrip.service;

import ce.daegu.ac.kr.aStartrip.dto.MemberDTO;
import ce.daegu.ac.kr.aStartrip.entity.Member;
import ce.daegu.ac.kr.aStartrip.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public boolean findID(String id) {
        if (memberRepository.findById(id).isEmpty()) {
            return false;
        }
        return true;
    }

    public void register(MemberDTO memberDTO) {
        Member entity = dtoToEntity(memberDTO);
        memberRepository.save(entity);
    }
}
