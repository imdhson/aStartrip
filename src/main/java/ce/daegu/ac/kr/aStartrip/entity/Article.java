package ce.daegu.ac.kr.aStartrip.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Article extends BaseEntity{
    @Id
    private Long num;
    private String title;
    private String writer;
    private String content;
    private long hit;
    private int type;
    private long root;
    private char grade;
}
