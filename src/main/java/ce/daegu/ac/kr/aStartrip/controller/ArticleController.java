package ce.daegu.ac.kr.aStartrip.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;

@Slf4j
@Controller
public class ArticleController {

    @GetMapping("/article")
    public String article() { // 새로운 학습 활동 시작
        log.debug("article()");
        return "article/article";
    }

    // reading -------

    @GetMapping("/article/r01")
    public String r01() {
        log.debug("r01()");
        return "article/r01";
    }

    @GetMapping("/article/r02")
    public String r02() {
        log.debug("r02()");
        return "article/r02";
    }

    // writing -------
    @GetMapping("/article/w01")
    public String w01() {
        log.debug("w01()");
        return "article/w01";
    }

    @GetMapping("/article/w02")
    public String w02() {
        log.debug("w02()");
        return "article/w02";
    }

    //vocabulary --------
    @GetMapping("/article/v01")
    public String v01() {
        log.debug("v01()");
        return "article/v01";
    }

    @GetMapping("/article/v02")
    public String v02() {
        log.debug("v02()");
        return "article/v02";
    }

    @GetMapping("/article/{num}")
    public String articleDetail(@PathVariable("num") String articleNum){
        log.debug("articleDetail({})", articleNum);
        return "article/article";
    }
}
