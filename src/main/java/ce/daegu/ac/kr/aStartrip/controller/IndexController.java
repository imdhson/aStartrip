package ce.daegu.ac.kr.aStartrip.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class IndexController {
    @GetMapping("/")
    public String index(Model model) {
        log.debug("index()");
        model.addAttribute("name", "test");
        return "index";
    }
}
