package ce.daegu.ac.kr.aStartrip.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
public class ErrorController {
    @GetMapping("/loginError")
    public String loginError(@RequestParam("msg") String msg, Model model) {
        model.addAttribute("msg", msg);
        return "error";
    }
}
