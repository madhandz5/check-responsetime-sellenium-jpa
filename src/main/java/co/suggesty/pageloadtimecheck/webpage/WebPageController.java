package co.suggesty.pageloadtimecheck.webpage;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class WebPageController {
    private final WebPageService webPageService;

    @GetMapping(value = "/page/all")
    public List<WebPage> getPages() {
        return webPageService.getPages();
    }

    @GetMapping(value = "/page/save/{pageName}")
    public void insertPage(@PathVariable("pageName") String pageName){
        webPageService.insertPage(pageName);
    }

    @GetMapping(value = "/page/remove/{id}")
    public void removePage(@PathVariable("id") Long id){
        webPageService.removePage(id);
    }
}
