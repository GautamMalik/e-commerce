package com.nagp.product.products;

import com.nagp.product.contstants.Constants;
import com.nagp.product.exceptions.BadInputException;

public class ProductFactory {
    public static Product createProduct(String category) {
        switch (category.toUpperCase()) {
            case "ELECTRONICS":
                return new ElectronicsProduct();
            case "SPORTS":
                return new SportsProduct();
            case "FASHION":
                return new FashionProduct();
            default:
                throw new BadInputException(Constants.INVALID_CATEGORY,"Unknown product category: " + category);
        }
    }
}
