package data;

public enum ApiErrorMessages {

    MISSING_TITLE("Field 'title' is required"),
    MISSING_AUTHOR("Field 'author' is required"),
    EMPTY_TITLE("Field 'title' cannot be empty"),
    EMPTY_AUTHOR("Field 'author' cannot be empty"),
    DUPLICATE_BOOK("Another book with similar title and author already exists"),
    BOOK_NOT_FOUND("Book not found"),
    FIELD_ID("Field 'id' is read-only");

    private final String message;

    ApiErrorMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
