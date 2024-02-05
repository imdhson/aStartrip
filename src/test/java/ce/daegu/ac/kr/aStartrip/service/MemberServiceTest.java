package ce.daegu.ac.kr.aStartrip.service;

import ce.daegu.ac.kr.aStartrip.dto.MemberDTO;
import ce.daegu.ac.kr.aStartrip.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;

@Slf4j
@SpringBootTest
public class MemberServiceTest {
    @Autowired
    private MemberService memberService;

    @Test
    public void registTest() {
        MemberDTO memberDTO = MemberDTO.builder()
                .email("1@mail.com")
                .PW("1")
                .name("testNAME").tel("11-1").address("address11")
                .activation(true).build();
        //memberService.register(memberDTO);
    }

}
