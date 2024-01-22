package ce.daegu.ac.kr.aStartrip.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Entity
@Data
@Builder
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    //부모 article
    @ManyToOne
    @JoinColumn(name = "article_num")
    private Article article;


    @NotNull
    @NotBlank
    @Enumerated(EnumType.STRING)
    private CardType cardType;

    @Enumerated(EnumType.STRING)
    private LLMStatus llmStatus = LLMStatus.NEW;

    private String UserInput0; //r02 | w01 | w02 | v01 | v02 | 댓글

    private String LLMResponse0; //r01 | r02-Pre | w01 | w02 | v01 | v02-동의어
    private String LLMResponse1; //r02-Background | v02-반의어
    private String LLMResponse2; //r02-Post | v02-word-family
}
