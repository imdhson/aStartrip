package ce.daegu.ac.kr.aStartrip.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Article extends BaseEntity {
    @Id
    @NotNull
    @NotBlank
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long num;
    private String title;
    private String content;
    private long hit;
    private int type;
    private long root;


    @ManyToOne
    @JoinColumn(name = "member_email")
    private Member member;

    @Builder.Default
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private List<Card> cardList = new ArrayList<>();


}
