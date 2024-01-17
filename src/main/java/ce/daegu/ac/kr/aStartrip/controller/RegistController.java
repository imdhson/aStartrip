package ce.daegu.ac.kr.aStartrip.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class RegistController {
    @GetMapping("/regist")
    public String regist() {
        log.info("regist form");
        return "regist";
    }
}
