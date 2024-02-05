package ce.daegu.ac.kr.aStartrip.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@ToString
public class Article extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long num;
    private String title;
    private long hit;
    @Enumerated(EnumType.STRING)
    private ArticlePermissionENUM articlePermission;

    @ManyToOne
    @JoinColumn(name = "member_email")
    @ToString.Exclude
    private Member member;

    public void addCard(Card card) {
        cardList.add(card);
        card.setArticle(this);
    }

    @Builder.Default
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @ToString.Exclude
    private List<Card> cardList = new ArrayList<>();
}
