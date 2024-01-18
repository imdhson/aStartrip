package ce.daegu.ac.kr.aStartrip.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class MemberDTO {
    private String name;
    private LocalDate birthDate;
    private String address;
    private String tel;
    private String ID;
    private String PW;
    private boolean activation;//계정 활성화 여부: 이메일 인증 등

    private LocalDateTime regDate;
    private LocalDateTime modDate;
    private LocalDateTime PWModDate;
    private char grade;
}