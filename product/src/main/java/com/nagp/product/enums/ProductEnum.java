package com.nagp.product.enums;

public enum ProductEnum {
    ELECTRONICS("Electronics"),
    SPORTS("Sports"),
    FASHION("Fashion");

    private String category;

    ProductEnum(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public static boolean contains(String category) {
        for (ProductEnum productEnum : ProductEnum.values()) {
            if (productEnum.getCategory().equalsIgnoreCase(category)) {
                return true;
            }
        }
        return false;
    }
}
