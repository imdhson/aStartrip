package ce.daegu.ac.kr.aStartrip.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    @Email(message = "올바른 이메일 주소를 입력해야 합니다.")
    private String email;
    private String PW;
    private boolean activation;//계정 활성화 여부: 이메일 인증 등
    private char grade;

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Article> articleList = new ArrayList<>();
    public void addArticle(Article article) {
        articleList.add(article);
        article.setMember(this);
    }
    private String authCode;
    //oauth2
    @Enumerated(value = EnumType.STRING)
    private Provider provider;
}
