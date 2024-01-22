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
    private String content;
    private long hit;
    private boolean visibleBoard; // 게시판에 업로드 된지 여부

    @ManyToOne
    @JoinColumn(name = "member_email")
    private Member member;

    @Builder.Default
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)

    private List<Card> cardList = new ArrayList<>();
    public void addCard(Card card) {
        cardList.add(card);
        card.setArticle(this);
    }
}
