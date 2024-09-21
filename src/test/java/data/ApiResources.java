package data;

public enum ApiResources {

    addBook("/books/"),
    getAllBooks("/books/"),
    getBook("/books/{id}/");

    private String resources;

    ApiResources(String resources)
    { // Constructor
        this.resources = resources;
    }

    public String getResources() {
        return resources;
    }


}