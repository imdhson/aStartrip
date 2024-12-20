package ce.daegu.ac.kr.aStartrip.service;

import ce.daegu.ac.kr.aStartrip.dto.ArticleDTO;
import ce.daegu.ac.kr.aStartrip.dto.CardDTO;
import ce.daegu.ac.kr.aStartrip.dto.MemberDTO;
import ce.daegu.ac.kr.aStartrip.entity.Article;
import ce.daegu.ac.kr.aStartrip.entity.Member;
import ce.daegu.ac.kr.aStartrip.entity.Provider;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public interface MemberService {

    boolean findID(String email);

    MemberDTO findMemberById(String email);

    List<ArticleDTO> userArticleList(String email);

    boolean sendCodeToEmail(String toEmail);

    boolean verifiedCode(String email, String authCode);

    boolean changePassword(MemberDTO dto);

    MemberDTO updateMember(MemberDTO memberDTO, Member member);

    default Member dtoToEntity(MemberDTO dto) {
        Member entity = Member.builder()
                .name(dto.getName())
                .birthDate(dto.getBirthDate())
                .address(dto.getAddress())
                .tel(dto.getTel())
                .email(dto.getEmail())
                .PW(dto.getPW())
                .authCode(dto.getAuthCode())
                .activation(dto.isActivation())
                .build();
        return entity;
    }

    default MemberDTO entityToDto(Member entity) {
        List<ArticleDTO> aList = new ArrayList<>();
        for (Article a : entity.getArticleList()) {
            ArticleDTO aa = ArticleDTO.builder()
                    .num(a.getNum())
                    .title(a.getTitle())
                    .writer(a.getMember().getName())
                    .hit(a.getHit())
                    .articlePermission(a.getArticlePermission())
                    .regDate(a.getRegDate())
                    .modDate(a.getModDate()).build();
            aList.add(aa);
        }
        MemberDTO dto = MemberDTO.builder()
                .name(entity.getName())
                .birthDate(entity.getBirthDate())
                .address(entity.getAddress())
                .tel(entity.getTel())
                .email(entity.getEmail())
                .PW(entity.getPW())
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .grade(entity.getGrade())
                .articleDTOList(aList)
                .authCode(entity.getAuthCode())
                .activation(entity.isActivation()).build();
        return dto;
    }

    Member register(MemberDTO memberDTO, Provider provider);
}
