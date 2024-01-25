package ce.daegu.ac.kr.aStartrip.service;

import ce.daegu.ac.kr.aStartrip.dto.ArticleDTO;
import ce.daegu.ac.kr.aStartrip.dto.MemberDTO;
import ce.daegu.ac.kr.aStartrip.entity.Member;
import ce.daegu.ac.kr.aStartrip.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final ArticleService articleService;

    @Override
    public boolean findID(String email) {
        if (memberRepository.findById(email).isEmpty()) {
            return false;
        }
        return true;
    }

    public void findUpdateTitleUser(String email, ArticleDTO dto) {
        Optional<Member> entity = memberRepository.findById(email);
        Member m = entity.get();
        articleService.changeArticle(m, dto);
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
