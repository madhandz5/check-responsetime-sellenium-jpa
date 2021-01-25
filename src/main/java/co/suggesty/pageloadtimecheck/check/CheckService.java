package co.suggesty.pageloadtimecheck.check;

import co.suggesty.pageloadtimecheck.page.Page;
import co.suggesty.pageloadtimecheck.page.PageRepository;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CheckService {

    private final PageRepository pageRepository;
    private final CheckRepository checkRepository;

    public void test() {
        //        System Property Setup
        System.setProperty("webdriver.chrome.driver", "/Users/ryan/Documents/chromedriver");
//        Driver Setup
        ChromeOptions options = new ChromeOptions();
        //    Web Driver Setting
        WebDriver driver = new ChromeDriver(options);
//      WebElement element;
        List<Page> checkPages = pageRepository.findAll();


        int index = 0;
        for (Page checkPage : checkPages) {
            System.out.println("checkPage.getPage_name() = " + checkPage.getPage_name());

        }
//        try {
//            int start = (int) System.currentTimeMillis();
//            driver.get();
//            int finish = (int) System.currentTimeMillis();
//            int loadingTime = finish - start;
//            System.out.println("=================================");
//            System.out.println("start = " + loadingTime);
//            System.out.println("=================================");
//            saveResult(checkPages.get(index++), loadingTime);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            driver.close();
//        }

    }

    private void saveResult(Page page, int loadingTime) {
        Check check = Check.builder().checkedAt(LocalDateTime.now()).time(loadingTime).pages(page)
                .build();
        checkRepository.save(check);
    }
}
