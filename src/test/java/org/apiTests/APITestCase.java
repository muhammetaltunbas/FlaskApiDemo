package org.apiTests;

import api.ApiErrorMessages;
import api.ApiResources;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;

public class APITestCase {
    public static String API_ROOT = "http://127.0.0.1:5000/api";
    public static RequestSpecification req;

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = API_ROOT;
    }

    public RequestSpecification getCommonReq() {
        if (req == null) {
            RestAssured.useRelaxedHTTPSValidation(); // SSLHandshakeException hatasını yok saymak için eklenmiştir.

            req = new RequestSpecBuilder()
                    .setContentType(ContentType.JSON)
                    .build();
        }
        return req;
    }

    public void executeApi(ApiResources apiResources, String method) {
        executeApi(apiResources, method, null); // Call the overloaded method with null bookId
    }

    public void executeApi(ApiResources apiResources, String method, Integer bookId) {
        String resourcePath = apiResources.getResources();

        if (method.equalsIgnoreCase("GET") && bookId != null) {
            resourcePath = resourcePath.replace("{id}", String.valueOf(bookId));
            BookApiTest.response = RestAssured.given().when().get(resourcePath);
        } else if (method.equalsIgnoreCase("PUT")) {
            BookApiTest.response = BookApiTest.request.when().put(resourcePath);
        } else if (method.equalsIgnoreCase("GET")) {
            BookApiTest.response = RestAssured.given().when().get(resourcePath);
        }
    }

    public String getJsonPath(Response response, String key) {
        String resp = response.asString();
        JsonPath js = new JsonPath(resp);
        return js.get(key).toString();
    }

    public void assertErrorResponse(String requestBody, ApiErrorMessages expectedError) {
        BookApiTest.request = RestAssured.given().spec(getCommonReq()).body(requestBody);
        executeApi(ApiResources.addBook, "PUT");

        Assert.assertEquals(BookApiTest.response.getStatusCode(), 400);
        Assert.assertEquals(getJsonPath(BookApiTest.response, "error"), expectedError.getMessage());
    }

}
