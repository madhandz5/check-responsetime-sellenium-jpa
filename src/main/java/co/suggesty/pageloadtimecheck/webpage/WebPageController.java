package co.suggesty.pageloadtimecheck.webpage;

import co.suggesty.pageloadtimecheck.check.Check;
import co.suggesty.pageloadtimecheck.check.CheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class WebPageController {
    private final WebPageService webPageService;
    private final CheckService checkService;

    @GetMapping(value = "/page/save")
    public String insertPage(){
        return "page/save";
    }

    @PostMapping (value = "/page/save")
    public String insertPage(@RequestParam String url){
        webPageService.insertPage(url);
        return "redirect:/page/list";
    }

    @GetMapping(value = "/page/remove/{id}")
    public String removePage(@PathVariable("id") Long id){
        WebPage webPage = webPageService.getPage(id);
        checkService.removeCheck(webPage);
        webPageService.removePage(id);
        return "redirect:/page/list";
    }

    @GetMapping(value = "/page/list")
    public String listupPage(Model model) {
        List<WebPage> urlList = webPageService.getPages();
        model.addAttribute("urlList", urlList);
        return "page/pageList";
    }
}
