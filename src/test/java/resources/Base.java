package resources;

import api.ApiResources;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;

public class Base {

    private static RequestSpecification req;

    public RequestSpecification getCommonRequest() {
        if (req == null) {
            req = new RequestSpecBuilder()
                    .setContentType(ContentType.JSON)
                    .build();
        }
        return req;
    }

    public Response executeApi(ApiResources apiResource, String method, Integer id, RequestSpecification requestSpec) {
        String resourcePath = apiResource.getResourcePath();

        if (id != null) {
            resourcePath = resourcePath.replace("{id}", String.valueOf(id));
        }

        switch (method.toUpperCase()) {
            case "GET":
                return requestSpec.when().get(resourcePath);
            case "POST":
                return requestSpec.when().post(resourcePath);
            case "PUT":
                return requestSpec.when().put(resourcePath);
            case "DELETE":
                return requestSpec.when().delete(resourcePath);
            default:
                throw new IllegalArgumentException("Invalid HTTP Method: " + method);
        }
    }

    public String getJsonPath(Response response, String key) {
        return response.jsonPath().getString(key);
    }

    public void assertDoubleEquals(double expected, double actual) {
        Assert.assertEquals(expected, actual, 0.1);
    }
    public void validateResponseStatus(Response response, int expectedStatus) {
        Assert.assertEquals(response.getStatusCode(), expectedStatus, "Unexpected status code!");
    }
}
