package co.suggesty.pageloadtimecheck.webpage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WebPageService {
    private final WebPageRepository webPageRepository;

    public List<WebPage> getPages() {
        List<WebPage> pageList = webPageRepository.findAll();
        return pageList;
    }

    public void insertPage(String pageName) {

        // check url
        int responseCode = 0;
        try {
            URL siteUrl = new URL("https://"+pageName);
            HttpURLConnection connection = (HttpURLConnection) siteUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            responseCode = connection.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // insert page
        if (responseCode == 200) {
            WebPage newPage = WebPage.builder()
                    .is_use(true)
                    .create_time(LocalDateTime.now())
                    .pageName(pageName)
                    .build();

            webPageRepository.save(newPage);
        }
    }

    public void removePage(Long id) {
        webPageRepository.deleteById(id);
    }

    public long countWebPages() {
        return webPageRepository.count();
    }
}
