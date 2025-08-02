package com.nagp.product.delegate;

import com.nagp.product.contstants.Constants;
import com.nagp.product.dto.CartRequestDTO;
import com.nagp.product.enums.ProductEnum;
import com.nagp.product.exceptions.BadInputException;
import com.nagp.product.products.Product;
import com.nagp.product.service.InventoryService;
import com.nagp.product.utils.AuthorizationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class InventoryDelegate {

    private final InventoryService inventoryService;
    private final AuthorizationUtils authorizationUtils;

    public List<Product> getProducts(String category, String name, String token) {
        log.info("Inside InventoryDelegate :: getProducts() method");
        isAuthorizedUser(token);
        if(!StringUtils.isEmpty(category) && !ProductEnum.contains(category)) {
            log.error("Invalid category: {}", category);
            throw new BadInputException(Constants.INVALID_CATEGORY,"Invalid category: " + category);
        }
        List<Product> products = inventoryService.getProducts(category, name, null);
        log.info("InventoryDelegate::getProducts ends successfully");
        return products;
    }

    private void isAuthorizedUser(String token) {
        if(StringUtils.isEmpty(token)) {
            log.error("Token is empty");
            throw new BadInputException(Constants.INVALID_TOKEN, "Token is empty");
        }
        authorizationUtils.isValidSession(token);
    }

    public String deleteProduct(String productId, String token) {
        log.info("Inside InventoryDelegate :: deleteProduct() method with productId: {}", productId);
        isAuthorizedUser(token);
        if(StringUtils.isEmpty(productId)) {
            log.error("Product ID is empty");
            throw new BadInputException(Constants.INVALID_TOKEN, "Product ID is empty");
        }
        String response = inventoryService.deleteProduct(productId);
        log.info("InventoryDelegate::deleteProduct ends successfully");
        return response;
    }

    public String canPlaceOrder(String productId, String quantity ,String token) {
        log.info("Inside InventoryDelegate :: canPlaceOrder() method with productId: {}", productId);
        isAuthorizedUser(token);
        if(StringUtils.isEmpty(productId)) {
            log.error("Product ID is empty");
            throw new BadInputException(Constants.INVALID_TOKEN, "Product ID is empty");
        }
        boolean response = inventoryService.canOrderProduct(new CartRequestDTO(Integer.parseInt(productId),extractUserIdFromToken(token),Integer.parseInt(quantity)));
        log.info("InventoryDelegate::canPlaceOrder ends successfully");
        if(!response)
            throw new BadInputException(Constants.INVALID_PRODUCT_ID, "Product with ID " + productId + " not found");


        return "Order can be placed";
    }
    private int extractUserIdFromToken(String token) {
        return Integer.parseInt(token.split(Constants.DELIMITER)[0]);
    }

}
