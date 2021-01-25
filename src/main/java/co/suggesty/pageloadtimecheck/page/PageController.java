package co.suggesty.pageloadtimecheck.page;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PageController {
    private final PageService pageService;

    @GetMapping(value = "/page/all")
    public List<Page> getPages() {
        return pageService.getPages();
    }

    @GetMapping(value = "/page/save/{pageName}")
    public void insertPage(@PathVariable("pageName") String pageName){
        pageService.insertPage(pageName);
    }

    @GetMapping(value = "/page/remove/{id}")
    public void removePage(@PathVariable("id") Long id){
        pageService.removePage(id);
    }
}
