package ce.daegu.ac.kr.aStartrip.dto;

import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ArticleDTO {
    private Long num;
    private String title;
    private String writer;
    private String content;
    private long hit;
    private int type;
    private long root;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
    private char grade;
}
