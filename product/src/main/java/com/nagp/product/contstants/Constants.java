package com.nagp.product.contstants;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class Constants {
    public static final String INVALID_CATEGORY = "INVALID.CATEGORY";
    public static final String INVALID_TOKEN = "INVALID.TOKEN";
    public static final String INVALID_PRODUCT_ID = "INVALID.PRODUCT.ID";
    public static final String INVALID_USER_ID = "INVALID.USER.ID";
    public static final String INVALID_PRODUCT = "INVALID.PRODUCT";
    public static final String INVALID_REQUEST = "INVALID.REQUEST";
    public static final String PRODUCT_ALREADY_IN_CART = "PRODUCT.ALREADY.IN.CART";
    public static final String INVALID_QUANTITY = "INVALID.QUANTITY";
    public static final String PRODUCT_ADDED_SUCCESSFULLY = "`PRODUCT.ADDED.SUCCESSFULLY";
    public static final String DELIMITER = "@";

    private Constants() {}
}
