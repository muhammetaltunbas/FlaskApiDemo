package uiTests;

import org.testng.Assert;
import org.testng.annotations.*;
import ui.uiClasses.BaseUI;
import ui.uiPages.LoginPage;

import java.io.IOException;

public class LoginTest extends BaseUI {
    public LoginPage lp;

    @Parameters("browser")
    @BeforeMethod
    public void goLoginPage(@Optional("chrome") String browser) throws IOException {
        driver = initializeDriver(browser);
        driver.get(LOGIN_URL);
    }

    @DataProvider(name = "loginData")
    public Object[][] loginDataProvider() {
        return new Object[][]{
                {"tofeva3141@exweme.com", "Test.1903*", true},
                {"invalidUser", "Test.1903*", false},
                {"tofeva3141@exweme.com", "invalidPass", false},
                {"", "", false}, // Empty fields
                {"tofeva3141@exweme.com", "", false}, // Empty password
                {"", "Test.1903*", false},  // Empty username
                {"", "Test.1903*", true}// fails on purposely
        };
    }

    @Test(dataProvider = "loginData")
    public void testLogin(String username, String password, boolean expectedSuccess) throws InterruptedException {
        lp = new LoginPage(driver);
        boolean loginSuccess = lp.performLogin(username, password);
        Assert.assertEquals(loginSuccess, expectedSuccess);
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

}
