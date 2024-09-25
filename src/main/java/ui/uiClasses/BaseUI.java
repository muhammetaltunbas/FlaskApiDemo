package ui.uiClasses;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class BaseUI {

    public static WebDriver driver;

    private BrowserFactory browserFactory;

    public WebDriver initializeDriver(String browserType) throws IOException {
        switch (browserType.toLowerCase()) {
            case "chrome":
                browserFactory = new ChromeBrowser();
                break;
            case "firefox":
                browserFactory = new FirefoxBrowser();
                break;
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browserType);
        }
        driver = browserFactory.initializeDriver();
        return driver;
    }

    public String getScreenShotPath(String testCaseName, WebDriver driver) throws IOException {
        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);
        String destinationFile = System.getProperty("user.dir") + "//reports//" + testCaseName + ".png";
        FileUtils.copyFile(source, new File(destinationFile));
        return destinationFile;

    }
}
