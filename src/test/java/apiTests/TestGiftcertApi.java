package apiTests;

import ApiClasses.Cart;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.*;
import resources.Base;
import static io.restassured.RestAssured.given;

public class TestGiftcertApi extends Base {

    private static final String BASE_URI = "http://127.0.0.1:5000/api";
    private Cart cart;

    @BeforeMethod
    public void setup() {
        cart = new Cart();
        RestAssured.baseURI = BASE_URI;
    }

    @Test(priority = 1)
    public void testAddToCart() {
        RequestSpecification requestSpec = given().spec(getCommonRequest());
        cart.addToCart(requestSpec);
        cart.deleteItem(requestSpec);
    }

    @Test(priority = 2)
    public void testApplyDiscountFlow() {
        RequestSpecification requestSpec = given().spec(getCommonRequest());
        cart.addToCart(requestSpec);
        cart.applyDiscount(requestSpec);
        cart.verifyCheckout(requestSpec);
        cart.removeDiscount(requestSpec);
        cart.deleteItem(requestSpec);
    }

    @AfterMethod
    public void checkForErrors() {
        if (cart.response != null && cart.response.getStatusCode() == 500) {
            Assert.fail("API returned a 500 Internal Server Error");
        }
    }
}
