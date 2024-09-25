package org.apiTests;

import api.ApiErrorMessages;
import api.ApiResources;
import api.Data;
import api.RandomTextGenerator;
import api.*;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.util.List;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class BookApiTest extends APITestCase {
    public static Response response;
    public static RequestSpecification request;
    private static int bookId;
    private String uniqueTitle;
    private String uniqueAuthor;


    @Test(priority = 0)
    public void testInitialBookStoreIsEmpty() {
        executeApi(ApiResources.getAllBooks, "GET");
        Assert.assertEquals(response.getStatusCode(), 200);
        List<?> responseBody = response.jsonPath().getList("$");
        Assert.assertEquals(responseBody.size(), 0);
    }

    @Test(priority = 1)
    public void testMissingAuthor() {
        assertErrorResponse(Data.missingAuthor(), ApiErrorMessages.MISSING_AUTHOR);
    }

    @Test(priority = 2)
    public void testMissingTitle() {
        assertErrorResponse(Data.missingTitle(), ApiErrorMessages.MISSING_TITLE);
    }

    @Test(priority = 3)
    public void testEmptyTitleFieldError() {
        assertErrorResponse(Data.emptyTitle(), ApiErrorMessages.EMPTY_TITLE);
    }

    @Test(priority = 4)
    public void testEmptyAuthorFieldError() {
        assertErrorResponse(Data.emptyAuthor(), ApiErrorMessages.EMPTY_AUTHOR);
    }

    @Test(priority = 5)
    public void testAddNewBook() {
        uniqueTitle = "Book Title " + RandomTextGenerator.generateRandomText();
        uniqueAuthor = "Author " + RandomTextGenerator.generateRandomText();
        request = given().spec(getCommonReq()).body(Data.addNewBook(uniqueTitle, uniqueAuthor));
        executeApi(ApiResources.addBook, "PUT");

        Assert.assertEquals(response.getStatusCode(), 201);
        Assert.assertEquals(getJsonPath(response, "author"), uniqueAuthor);
        Assert.assertEquals(getJsonPath(response, "title"), uniqueTitle);
        bookId = Integer.parseInt(getJsonPath(response, "id"));
    }

    @Test(priority = 6)
    public void testGetBookById() {
        testAddNewBook();
        executeApi(ApiResources.getBook, "GET", bookId);

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(getJsonPath(response, "author"), uniqueAuthor);
        Assert.assertEquals(getJsonPath(response, "title"), uniqueTitle);
        Assert.assertEquals(getJsonPath(response, "id"), String.valueOf(bookId));
    }

    @Test(priority = 7)
    public void testAddDuplicateBook() {
        testAddNewBook();
        assertErrorResponse(Data.addNewBook(uniqueTitle, uniqueAuthor), ApiErrorMessages.DUPLICATE_BOOK);
    }

    @Test(priority = 8)
    public void testGetBookByInvalidId() {
        int invalidBookId = 1903;
        executeApi(ApiResources.getBook, "GET", invalidBookId);
        Assert.assertEquals(response.getStatusCode(), 404);
        String responseBody = response.getBody().asString();
        Assert.assertTrue(responseBody.contains(ApiErrorMessages.BOOK_NOT_FOUND.getMessage()));
    }

    @Test(priority = 9)
    public void testSendIdObjectInPayload() {
        uniqueTitle = "Book Title " + RandomTextGenerator.generateRandomText();
        uniqueAuthor = "Author " + RandomTextGenerator.generateRandomText();
        int randomBookID = 190300;
        assertErrorResponse(Data.addNewBookWithId(uniqueTitle, uniqueAuthor, randomBookID), ApiErrorMessages.FIELD_ID);

    }

    @Test(priority = 10)
    public void testGetBookResponseSchema() {
        executeApi(ApiResources.getAllBooks, "GET");
        Assert.assertEquals(response.getStatusCode(), 200);
        response.then().assertThat()
                .body(matchesJsonSchemaInClasspath("book-schema.json"));
    }

    @AfterMethod
    public void checkForInternalServerError() {
        if (response != null && response.getStatusCode() == 500) {
            Assert.fail("API returned a 500 Internal Server Error, this should never happen.");
        }
    }


}
