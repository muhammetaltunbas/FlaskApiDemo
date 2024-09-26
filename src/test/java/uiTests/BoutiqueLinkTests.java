package uiTests;

import data.StaticTexts;
import org.testng.annotations.*;
import ui.uiClasses.BaseUI;
import ui.uiPages.HomePage;

import java.io.IOException;

public class BoutiqueLinkTests extends BaseUI {

    public HomePage hp;
    @Parameters("browser")
    @BeforeMethod
    public void goLoginPage(@Optional("chrome") String browser) throws IOException {
        driver = initializeDriver(browser);
        driver.get(BASE_URL);
    }

    @Test
    public void checkBoutiqueLinks() throws IOException {
        hp= new HomePage(driver);
        hp.getBoutiqueLists();
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }


}
