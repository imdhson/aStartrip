package ce.daegu.ac.kr.aStartrip.dto;

import ce.daegu.ac.kr.aStartrip.entity.ArticlePermissionENUM;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.hibernate.mapping.Array;
import org.hibernate.mapping.List;

import ce.daegu.ac.kr.aStartrip.entity.Member;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ArticleDTO {
    private Long num;
    private String title;
    private String writer;
    private long hit;
    private ArticlePermissionENUM articlePermission;

    private LocalDateTime regDate;
    private LocalDateTime modDate;

    private ArrayList<CardDTO> cardDTOList;
}
