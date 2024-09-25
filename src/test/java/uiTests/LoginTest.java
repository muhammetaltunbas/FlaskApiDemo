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
    public void go_loginPage(@Optional("chrome") String browser) throws IOException {
        driver = initializeDriver(browser);
        driver.get("https://www.trendyol.com/giris");
    }

    @DataProvider(name = "loginData")
    public Object[][] loginDataProvider() {
        return new Object[][]{
                {"tofeva3141@exweme.com", "Test.1903*", true},
                {"invalidUser", "validPass", false},
                {"validUser", "invalidPass", false},
                {"", "", false}, // Empty fields
                {"validUser", "", false}, // Empty password
                {"", "validPass", false}  // Empty username
        };
    }

    @Test(dataProvider = "loginData")
    public void testLogin(String username, String password, boolean expectedSuccess) {
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
