package ce.daegu.ac.kr.aStartrip.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class MemberDTO {
    private String name;
    private String ID;
    private String PW;
}