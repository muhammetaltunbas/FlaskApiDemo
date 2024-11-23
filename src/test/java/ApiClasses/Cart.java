package ApiClasses;

import api.ApiData;
import api.ApiResources;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import resources.Base;

public class Cart extends Base {
    public static Response response;

    private int itemId;
    private final String productName = "Macbook Pro-2";
    private final int productPrice = 25000;
    private final String discountCode = "DISCOUNT50";

    public Response addToCart(RequestSpecification req)
    {
        response = executeApi(
                ApiResources.ADD_ITEM,
                "PUT",
                null,
                req.body(ApiData.addNewItem(productName, productPrice))
        );
        validateResponseStatus(response,201);
        itemId = Integer.parseInt(getJsonPath(response, "id"));
        return response;
    }

    public Response applyDiscount(RequestSpecification req) {
        response = executeApi(
                ApiResources.APPLY_DISCOUNT,
                "POST",
                null,
                req.body(ApiData.applyDiscount(discountCode))
        );
        validateDiscountResponse();
        return response;
    }

    public Response verifyCheckout(RequestSpecification req)
    {
        response = executeApi(ApiResources.GET_CHECKOUT, "GET", null, req);
        validateDiscountResponse();
        return response;
    }
    public Response removeDiscount(RequestSpecification req)
    {
        response = executeApi(ApiResources.REMOVE_DISCOUNT, "POST", null, req);
        Assert.assertEquals(getJsonPath(response, "message"), "Discount removed successfully");
        return response;
    }
    public Response deleteItem(RequestSpecification req)
    {
        response = executeApi(ApiResources.DELETE_ITEM, "DELETE", itemId, req);
        Assert.assertEquals(getJsonPath(response, "message"), "Item removed successfully");
        return response;
    }

    private void validateDiscountResponse() {
        validateResponseStatus(response,200);
        assertDoubleEquals(productPrice / 2, response.jsonPath().getDouble("discount_amount"));
        Assert.assertEquals(getJsonPath(response, "discount_code"), discountCode);
        assertDoubleEquals(productPrice - productPrice / 2, response.jsonPath().getDouble("final_price"));
    }



}
