package api;

public class ApiData {

    public static String bookTitle = RandomTextGenerator.generateRandomText();
    public static String bookAuthor = RandomTextGenerator.generateRandomText();

    public static String missingTitle() {
        return "{ \"author\": \"Author Name\" }";
    }

    public static String missingAuthor() {
        return "{ \"title\": \"Automation Testing\" }";
    }

    public static String emptyTitle() {
        return "{\"author\": \"Author Name\", \"title\": \"\" }";
    }

    public static String emptyAuthor() {
        return "{\"title\": \"Title Info\", \"author\": \"\" }";
    }


    public static String addNewBook(String title, String author) {

        return "{\n" +
                "    \"title\": \"" + title + "\",\n" +
                "    \"author\": \"" + author + "\"\n" +
                "}";
    }

    public static String addNewBookWithId(String title, String author, int bookID) {

        return "{\n" +
                "    \"title\": \"" + title + "\",\n" +
                "    \"author\": \"" + author + "\",\n" +
                "    \"id\": \"" + bookID + "\"\n" +
                "}";
    }


}
