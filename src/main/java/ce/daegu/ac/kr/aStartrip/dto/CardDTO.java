package ce.daegu.ac.kr.aStartrip.dto;

import ce.daegu.ac.kr.aStartrip.entity.Article;
import ce.daegu.ac.kr.aStartrip.entity.CardType;
import ce.daegu.ac.kr.aStartrip.entity.LLMStatus;

public class CardDTO {
    private long id;
    private Article article;
    private CardType cardType;
    private LLMStatus llmStatus;
    private String UserInput0;//r02 | w01 | w02 | v01 | v02 | 댓글
    private String LLMResponse0;//r01 | r02-Pre | w01 | w02 | v01 | v02-동의어
    private String LLMResponse1;//r02-Background | v02-반의어
    private String LLMResponse2;//r02-Post | v02-word-family
}
