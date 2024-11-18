package api;

public enum ApiResources {
    ADD_ITEM("/cart/"),
    APPLY_DISCOUNT("/cart/apply_discount/"),
    GET_CHECKOUT("/cart/checkout/"),
    DELETE_ITEM("/cart/{id}/"),
    REMOVE_DISCOUNT("/cart/remove_discount/");

    private final String resourcePath;

    ApiResources(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    public String getResourcePath() {
        return resourcePath;
    }
}
