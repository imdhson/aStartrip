package ce.daegu.ac.kr.aStartrip.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PasswordDTO {
    private String name;
    private LocalDate birthdate;

    private String email;
    private String PW;
    private String PW1;
}
