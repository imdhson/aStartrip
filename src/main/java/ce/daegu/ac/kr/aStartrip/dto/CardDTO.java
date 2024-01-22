package ce.daegu.ac.kr.aStartrip.dto;

import ce.daegu.ac.kr.aStartrip.entity.CardTypeENUM;
import ce.daegu.ac.kr.aStartrip.entity.LLMStatusENUM;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class CardDTO {
    private long id;
    private long articleNum;
    private CardTypeENUM cardType;
    private LLMStatusENUM llmStatus;
    private String UserInput0;//r02 | w01 | w02 | v01 | v02 | 댓글
    private String LLMResponse0;//r01 | r02-Pre | w01 | w02 | v01 | v02-동의어
    private String LLMResponse1;//r02-Background | v02-반의어
    private String LLMResponse2;//r02-Post | v02-word-family
}
