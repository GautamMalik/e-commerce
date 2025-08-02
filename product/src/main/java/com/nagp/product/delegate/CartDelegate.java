package com.nagp.product.delegate;

import com.nagp.product.contstants.Constants;
import com.nagp.product.dto.CartRequestDTO;
import com.nagp.product.exceptions.BadInputException;
import com.nagp.product.products.Product;
import com.nagp.product.service.CartService;
import com.nagp.product.utils.AuthorizationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class CartDelegate {

    private final CartService cartService;
    private final AuthorizationUtils authorizationUtils;

    public List<Product> getProducts(String token, String category, String name, Integer id) {
        log.info("Inside CartDelegate :: getProducts() method");
        isAuthorizedUser(token);

        int userId = extractUserIdFromToken(token);

        List<Product> product = cartService.getProducts(userId, category, name, id);
        if(product == null || product.isEmpty()) {
            log.error("No products found for user ID: {}", userId);
            product = new ArrayList<>();
        }
        log.info("CartDelegate::getProducts ends successfully");
        return product;
    }

    public String addProduct(CartRequestDTO cartRequestDTO, String token) {
        log.info("Inside CartDelegate :: addProduct() method with cartRequestDTO: {}", cartRequestDTO);
        isAuthorizedUser(token);
        cartRequestDTO.setUserId(extractUserIdFromToken(token));
        if(cartRequestDTO.getProductId() == null) {
            log.error("Product is empty");
            throw new BadInputException(Constants.INVALID_PRODUCT_ID, "Invalid product ID");
        }
        if(cartRequestDTO.getQuantity() == null) {
            log.error("Quantity is empty");
            throw new BadInputException(Constants.INVALID_QUANTITY, "Invalid quantity");
        }
        String response = cartService.addProduct(cartRequestDTO);
        log.info("CartDelegate::addProduct ends successfully");
        return response;
    }

    private void isAuthorizedUser(String token) {
        if(StringUtils.isEmpty(token)) {
            log.error("Token is empty");
            throw new BadInputException(Constants.INVALID_TOKEN, "Token is empty");
        }
        authorizationUtils.isValidSession(token);
    }

    public String deleteProduct(String token) {
        log.info("Inside CartDelegate :: deleteProduct() method");
        isAuthorizedUser(token);
        int userId = extractUserIdFromToken(token);
        String response = cartService.deleteProduct(userId,null);
        log.info("CartDelegate::deleteProduct ends successfully");
        return response;
    }

    private int extractUserIdFromToken(String token) {
        return Integer.parseInt(token.split(Constants.DELIMITER)[0]);
    }

    public List<Product> checkout(String productId, String token) {
        log.info("Inside CartDelegate :: checkout() method with productId: {}", productId);
        int userId = extractUserIdFromToken(token);
        List<Product> products = new ArrayList<>();
        if (StringUtils.isEmpty(productId)) {
            products = cartService.getProducts(userId, null, null, null);
            if(!products.isEmpty())
                cartService.deleteProduct(userId, null);
        } else {
            products = cartService.getProducts(userId, null, null, Integer.valueOf(productId));
            if(!products.isEmpty())
                cartService.deleteProduct(userId, productId);
        }
        log.info("CartDelegate::checkout ends successfully");
        return products;
    }
}
