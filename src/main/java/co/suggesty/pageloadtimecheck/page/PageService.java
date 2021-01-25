package co.suggesty.pageloadtimecheck.page;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PageService {
    private final PageRepository pageRepository;

    public List<Page> getPages() {
        List<Page> pageList = pageRepository.findAll();
        return pageList;
    }

    public void insertPage(String pageName) {
        Page newPage = Page.builder()
                .is_use(true)
                .create_time(LocalDateTime.now())
                .page_name(pageName)
                .build();

        pageRepository.save(newPage);
    }

    public void removePage(Long id) {
        pageRepository.deleteById(id);
    }
}
