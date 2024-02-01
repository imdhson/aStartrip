package ce.daegu.ac.kr.aStartrip.dto;

import ce.daegu.ac.kr.aStartrip.entity.Article;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
    private String email;
    private String PW;
    private boolean activation;//계정 활성화 여부: 이메일 인증 등
    private List<ArticleDTO> articleDTOList;

    private LocalDateTime regDate;
    private LocalDateTime modDate;
    private char grade;
    private String authCode;
}