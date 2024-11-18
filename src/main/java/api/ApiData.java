package api;

public class ApiData {

    public static String addNewItem(String name, int price, int quantity) {
        return "{\n" +
                "    \"name\": \"" + name + "\",\n" +
                "    \"price\": " + price + ",\n" +
                "    \"quantity\": " + quantity + "\n" +
                "}";
    }

    public static String addNewItem(String name, int price) {
        return addNewItem(name, price, 1);
    }

    public static String applyDiscount(String discountCode) {
        return "{\n" +
                "    \"discount_code\": \"" + discountCode + "\"\n" +
                "}";
    }
}
