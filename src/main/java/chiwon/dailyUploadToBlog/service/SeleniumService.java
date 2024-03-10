package chiwon.dailyUploadToBlog.service;

import chiwon.dailyUploadToBlog.dto.CrolledItemInfo;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
public class SeleniumService {

    public void crolledDescriptionImgWithSelenium(CrolledItemInfo crolledItemInfo) {

        WebDriver driver = new ChromeDriver();
        String checkImagesLoadedScript =
                "var images = document.querySelectorAll('.vendor-item img');" +
                        "for (var i = 0; i < images.length; i++) {" +
                        "  if (!images[i].complete) {" +
                        "    return false;" +
                        "  }" +
                        "}" +
                        "return true;";


        driver.get(crolledItemInfo.getItemUrl());

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10000));
        WebElement button = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".product-detail-seemore-btn")));
        button.click();

        wait = new WebDriverWait(driver, Duration.ofSeconds(10000));
        wait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript(checkImagesLoadedScript).equals(true));
        List<WebElement> elements = driver.findElements(By.cssSelector(".vendor-item img"));

        for (WebElement element : elements) {
            String link = element.getAttribute("src");
            crolledItemInfo.getDescriptionsImgLink().add(link);
        }
        driver.quit();
    }

    public void crolledReviewWithSelenium(CrolledItemInfo crolledItemInfo) {
        WebDriver driver = new ChromeDriver();

        driver.get(crolledItemInfo.getItemUrl());

        int curPage = 1;
        int totalReview;

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1000));
        WebElement totalReviewElement = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".count")));
        String totalReviewString = totalReviewElement.getText();
        totalReviewString = totalReviewString.substring(0,totalReviewString.length() - 5);
        totalReviewString = totalReviewString.replace(",", "");
        totalReview = Integer.parseInt(totalReviewString);

        if (totalReview >= 10)
            totalReview = 10;
        try {
            Thread.sleep(1500); //1.5초 대기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        totalReviewElement.click();

        for (int curReview = 1 ; curReview <= totalReview ; curReview++) {

            wait = new WebDriverWait(driver, Duration.ofSeconds(1000));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.sdp-review__article__list__review__content")));
            List<WebElement> pageReviews = driver.findElements(By.cssSelector("div.sdp-review__article__list__review__content"));
            for (WebElement pageReview : pageReviews) {
                crolledItemInfo.getReviews().add("review[" + String.valueOf(curReview) + "]: " + pageReview.getText());
                if (++curReview > totalReview)
                    break;
            }
            // 버튼 누르기
            if (curReview <= totalReview) {
                String newPage = "button.sdp-review__article__page__num[data-page='" + String.valueOf(++curPage) +"']";
                WebElement newPageElement =  driver.findElement(By.cssSelector(newPage));
                newPageElement.click();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            curReview--;
        }
        driver.quit();

    }

}
