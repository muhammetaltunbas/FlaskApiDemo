package ui.uiPages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import ui.uiClasses.BaseUI;

public class LoginPage extends BaseUI {
    private WebDriver driver;

    // Locators
    private By userEmail = By.id("login-email");
    private By userPassword = By.id("login-password-input");
    private By loginBtn = By.cssSelector("[type='submit']");
    private By errorMessage = By.id("error-box-wrapper");


    private String homepageTitle = "En Trend Ürünler Türkiye'nin Online Alışveriş Sitesi Trendyol'da";

    // Constructor
    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    // Page Elements
    private WebElement getUserEmailField() {
        return driver.findElement(userEmail);
    }

    private WebElement getUserPasswordField() {
        return driver.findElement(userPassword);
    }

    private WebElement getLoginButton() {
        return driver.findElement(loginBtn);
    }

    private WebElement getErrorMessage() {
        return driver.findElement(errorMessage);
    }


    public boolean performLogin(String username, String password) {
        enterCredentials(username, password);
        getLoginButton().click();

        if (isHomepageDisplayed()) {
            return true; // Login successful
        }

        return !isErrorMessageDisplayed();
    }

    private void enterCredentials(String username, String password) {
        getUserEmailField().clear();
        getUserEmailField().sendKeys(username);
        getUserPasswordField().clear();
        getUserPasswordField().sendKeys(password);
    }

    private boolean isErrorMessageDisplayed() {
        return isElementPresent(errorMessage) && getErrorMessage().isDisplayed();
    }

    private boolean isHomepageDisplayed() {
        String actualTitle = driver.getTitle();
        return actualTitle.contains(homepageTitle);
    }

    private boolean isElementPresent(By locator) {
        return driver.findElements(locator).size() > 0;
    }
}
