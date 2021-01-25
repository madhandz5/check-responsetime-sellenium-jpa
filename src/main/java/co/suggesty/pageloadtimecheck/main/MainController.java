package co.suggesty.pageloadtimecheck.main;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping(value = "/")
    public String home(){
        return "index";
    }
}