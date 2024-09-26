package ui.uiClasses;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class FirefoxBrowser implements BrowserFactory {

    @Override
    public WebDriver initializeDriver() {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-notifications");
        WebDriver driver = new FirefoxDriver();
        driver.manage().window().maximize();
        return driver;
    }
}