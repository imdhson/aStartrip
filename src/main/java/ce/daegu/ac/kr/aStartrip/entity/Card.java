package ce.daegu.ac.kr.aStartrip.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //부모 article
    @ManyToOne
    @JoinColumn(name = "article_num")
    private Article article;


    @NotNull
    @Enumerated(EnumType.STRING)
    private CardTypeENUM cardType;

    @Enumerated(EnumType.STRING)
    private LLMStatusENUM llmStatus = LLMStatusENUM.NEW;

    @Column(length = 10000)
    private String UserInput0; //r02 | w01 | w02 | v01 | v02 | 댓글
    @Column(length = 10000)
    private String LLMResponse0; //r01 | r02-Pre | w01 | w02 | v01 | v02-동의어
    @Column(length = 10000)
    private String LLMResponse1; //r02-Background | v02-반의어
    @Column(length = 10000)
    private String LLMResponse2; //r02-Post | v02-word-family
}
