package co.suggesty.pageloadtimecheck.main;

import co.suggesty.pageloadtimecheck.member.CurrentMember;
import co.suggesty.pageloadtimecheck.member.Member;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping(value = "/")
    public String home(@CurrentMember Member member, Model model) {
        if (member != null) {
            model.addAttribute("member",member);
            return "index-after-login";
        }
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
