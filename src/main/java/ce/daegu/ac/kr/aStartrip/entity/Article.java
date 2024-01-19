package ce.daegu.ac.kr.aStartrip.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Article extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long num;
    private String title;
    private String content;
    private long hit;
    private int type;
    private long root;

    //private char grade;
    //private String writer;
    @ManyToOne
    @JoinColumn(name = "member_email")
    private Member member;

}
