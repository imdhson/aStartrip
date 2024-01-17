package ce.daegu.ac.kr.aStartrip.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class ArticleController {
    
    @GetMapping("/article")
    public String article(){ // 새로운 학습 활동 시작
        log.debug("article()");
        return "article";
    }
    
}
