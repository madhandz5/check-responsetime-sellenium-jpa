package co.suggesty.pageloadtimecheck.webpage;

import co.suggesty.pageloadtimecheck.check.CheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class WebPageController {
    private final WebPageService webPageService;
    private final CheckService checkService;

    @GetMapping(value = "/page/save/{pageName}")
    public void insertPage(@PathVariable("pageName") String pageName){
        webPageService.insertPage(pageName);
    }

    @GetMapping(value = "/page/remove/{id}")
    public void removePage(@PathVariable("id") Long id){
        WebPage webPage = webPageService.getPage(id);
        checkService.removeCheck(webPage);
        webPageService.removePage(id);
    }

    @GetMapping(value = "/page/list")
    public StringBuilder listupPage(Model model) {
        List<WebPage> urlList = webPageService.getPages();
        StringBuilder result = new StringBuilder();

        for (WebPage url: urlList) {
            result.append("<p>id: " + url.getId() + " / url: " + url.getPageName() + "<p>");
            result.append("<br>");
        }
        return result;
    }
}
