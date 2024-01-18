package ce.daegu.ac.kr.aStartrip.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member extends BaseEntity{
    private String name;
    private LocalDate birthDate;
    private String address;
    private String tel;
    @Id
    private String email;
    private String PW;

    private char grade;
}
