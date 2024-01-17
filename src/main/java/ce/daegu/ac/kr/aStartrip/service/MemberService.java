package ce.daegu.ac.kr.aStartrip.service;

import ce.daegu.ac.kr.aStartrip.dto.MemberDTO;
import ce.daegu.ac.kr.aStartrip.entity.Member;

public interface MemberService {
    default Member dtoToEntity(MemberDTO dto) {
        Member entity = Member.builder()
                .name(dto.getName())
                .birthDate(dto.getBirthDate())
                .address(dto.getAddress())
                .tel(dto.getTel())
                .ID(dto.getID())
                .PW(dto.getPW()).build();
        return entity;
    }

    default MemberDTO entityToDto(Member entity) {
        MemberDTO dto = MemberDTO.builder()
                .name(entity.getName())
                .birthDate(entity.getBirthDate())
                .address(entity.getAddress())
                .tel(entity.getTel())
                .ID(entity.getID())
                .PW(entity.getPW())
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .PWModDate(entity.getPWModDate())
                .grade(entity.getGrade()).build();
        return dto;
    }
}
