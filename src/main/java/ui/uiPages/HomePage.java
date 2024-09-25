package ui.uiPages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.List;
import java.util.stream.Collectors;

public class HomePage {
    private WebDriver driver;

    public HomePage(WebDriver driver) {
        this.driver = driver;
    }

    public List<String> getAllBoutiqueLinks() {
        List<WebElement> links = driver.findElements(By.cssSelector("your-css-selector"));
        return links.stream().map(e -> e.getAttribute("href")).collect(Collectors.toList());
    }
}
