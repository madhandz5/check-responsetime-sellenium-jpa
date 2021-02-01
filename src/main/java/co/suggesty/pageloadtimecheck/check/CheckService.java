package co.suggesty.pageloadtimecheck.check;

import co.suggesty.pageloadtimecheck.webpage.WebPage;
import co.suggesty.pageloadtimecheck.webpage.WebPageRepository;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CheckService {

    private final WebPageRepository webPageRepository;
    private final CheckRepository checkRepository;

    public void timeChecking() {
        // System Property Setup
        System.setProperty("webdriver.chrome.driver", "/opt/WebDriver/bin/chromedriver");
        // Driver Setup
        ChromeOptions options = new ChromeOptions();
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        // Web Driver Setting
        WebDriver driver = new ChromeDriver(options);
        // WebElement element;
        List<WebPage> checkPages = webPageRepository.findAll();

        try {
            for (WebPage checkPage : checkPages) {
                System.out.println("checkPage.getPageName() = " + checkPage.getPageName());

                int start = (int) System.currentTimeMillis();
                driver.get("https://" + checkPage.getPageName());
                int finish = (int) System.currentTimeMillis();
                int loadingTime = finish - start;

                System.out.println("=================================");
                System.out.println("loadingTime = " + loadingTime);
                System.out.println("=================================");
                saveResult(checkPage, loadingTime);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }

    private void saveResult(WebPage webPage, int loadingTime) {
        Check check = Check.builder().checkedAt(LocalDateTime.now()).time(loadingTime).webPage(webPage)
                .build();
        checkRepository.save(check);
    }

    public Page<Check> getCheckList(Pageable pageable) {
        return checkRepository.findAll(pageable);
    }

    public void removeCheck(WebPage webPage) {
        checkRepository.deleteByWebPage(webPage);
    }
}
