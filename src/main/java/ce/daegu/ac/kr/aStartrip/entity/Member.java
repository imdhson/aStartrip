package ce.daegu.ac.kr.aStartrip.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member extends BaseEntity {
    private String name;
    private LocalDate birthDate;
    private String address;
    private String tel;
    @Id
    @NotBlank
    @Email(message = "올바른 이메일 주소를 입력해야 합니다.")
    private String email;
    private String PW;
    private boolean activation;//계정 활성화 여부: 이메일 인증 등
    private char grade;

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private ArrayList<Article> articleList = new ArrayList<>();
}
