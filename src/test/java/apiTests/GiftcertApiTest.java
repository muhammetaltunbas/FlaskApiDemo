package apiTests;

import api.ApiData;
import api.ApiResources;
import resources.Base;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class GiftcertApiTest extends Base {
    private static final String BASE_URI = "http://127.0.0.1:5000/api";
    private static Response response;
    private static RequestSpecification requestSpec;
    private int itemId;
    private final String productName = "Macbook Pro-2";
    private final int productPrice = 25000;
    private final String discountCode = "DISCOUNT50";

    @BeforeMethod
    public void setup() {
        RestAssured.baseURI = BASE_URI;
        requestSpec = given().spec(getCommonRequest());
    }

    @Test(priority = 1)
    public void applyGiftcertTest() {
        // Add to cart
        response = executeApi(
                ApiResources.ADD_ITEM,
                "PUT",
                null,
                requestSpec.body(ApiData.addNewItem(productName, productPrice))
        );
        validateResponseStatus(response,201);
        itemId = Integer.parseInt(getJsonPath(response, "id"));

        // Apply discount
        response = executeApi(
                ApiResources.APPLY_DISCOUNT,
                "POST",
                null,
                requestSpec.body(ApiData.applyDiscount(discountCode))
        );
        validateDiscountResponse();

        // Verify checkout
        response = executeApi(ApiResources.GET_CHECKOUT, "GET", null, requestSpec);
        validateDiscountResponse();

        // Remove discount
        response = executeApi(ApiResources.REMOVE_DISCOUNT, "POST", null, requestSpec);
        Assert.assertEquals(getJsonPath(response, "message"), "Discount removed successfully");

        // Delete item
        response = executeApi(ApiResources.DELETE_ITEM, "DELETE", itemId, requestSpec);
        Assert.assertEquals(getJsonPath(response, "message"), "Item removed successfully");
    }

    private void validateDiscountResponse() {
        validateResponseStatus(response,200);
        assertDoubleEquals(productPrice / 2, response.jsonPath().getDouble("discount_amount"));
        Assert.assertEquals(getJsonPath(response, "discount_code"), discountCode);
        assertDoubleEquals(productPrice - productPrice / 2, response.jsonPath().getDouble("final_price"));
    }

    @AfterMethod
    public void checkForErrors() {
        if (response != null && response.getStatusCode() == 500) {
            Assert.fail("API returned a 500 Internal Server Error");
        }
    }
}
