package co.suggesty.pageloadtimecheck.check;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class CheckController {
    private final CheckService checkService;

    @GetMapping(value = "/check/all")
    public String checkResponse(Model model) {
        checkService.test();
        model.addAttribute("done","Done");
        return "page/check";
    }
}
