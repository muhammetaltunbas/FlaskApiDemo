package ui.uiPages;

import data.StaticTexts;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HomePage {
    private WebDriver driver;
    private int numberOfScrolls = 1;
    private WebDriverWait wait;
    private By boutiqueList = By.cssSelector("[data-displaytype*='BOUTIQUE']");
    private By closeBtn = By.cssSelector("[class*='modal-close']");
    private By allImages = By.cssSelector("[data-displaytype*='BOUTIQUE'] img");

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void getBoutiqueLists() throws IOException {
        closeAdIfVisible();

        Set<String> recordedUrls = new HashSet<>();
        for (int i = 0; i < numberOfScrolls; i++) {
            scrollToBottom();
            waitForPageLoad();

            List<WebElement> boutiqueLinks = driver.findElements(boutiqueList);
            for (WebElement link : boutiqueLinks) {
                String url = link.getAttribute("href");
                if (recordedUrls.add(url)) {
                    int responseCode = getResponseCode(url);
                    writeToCSV(link.getText(), url, responseCode, -1);
                }
            }

            List<WebElement> images = driver.findElements(allImages);
            for (WebElement image : images) {
                long loadDuration = measureImageLoadTime(image);
                String imageUrl = image.getAttribute("src");
                int imageResponseCode = getResponseCode(imageUrl);
                writeToCSV(image.getText(), imageUrl, imageResponseCode, loadDuration);
            }
        }
    }

    private void closeAdIfVisible() {
        try {
            WebElement closeAdButton = wait.until(ExpectedConditions.visibilityOfElementLocated(closeBtn));
            if (closeAdButton.isDisplayed()) {
                closeAdButton.click();
            }
        } catch (Exception e) {
            System.err.println("Ad close button not interactable: " + e.getMessage());
        }
    }

    private void scrollToBottom() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0, document.body.scrollHeight);");
    }

    private void waitForPageLoad() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int getResponseCode(String urlString) {
        int responseCode = -1;
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(urlString).openConnection();
            connection.setRequestMethod("GET");
            responseCode = connection.getResponseCode();
        } catch (IOException e) {
            System.err.println("Failed to get response code for: " + urlString);
        }
        return responseCode;
    }

    public long measureImageLoadTime(WebElement image) {
        long startTime = System.currentTimeMillis();
        waitUntilImageLoaded(image);
        return System.currentTimeMillis() - startTime;
    }

    public void waitUntilImageLoaded(WebElement image) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        for (int i = 0; i < 10; i++) {
            boolean isLoaded = (Boolean) js.executeScript(
                    "return arguments[0].complete && arguments[0].naturalWidth > 0;", image);
            if (isLoaded) {
                break;
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void writeToCSV(String name, String url, int responseCode, long loadDuration) throws IOException {
        try (FileWriter csvWriter = new FileWriter(StaticTexts.CSV_FILE_NAME, true)) {
            csvWriter.append(name)
                    .append(",")
                    .append(url)
                    .append(",")
                    .append(String.valueOf(responseCode))
                    .append(",")
                    .append(loadDuration != -1 ? String.valueOf(loadDuration) : "N/A")
                    .append("\n");
        }
    }
}
