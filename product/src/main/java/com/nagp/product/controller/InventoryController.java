package com.nagp.product.controller;

import com.nagp.product.delegate.InventoryDelegate;
import com.nagp.product.dto.ResponseTO;
import com.nagp.product.exceptions.BadInputException;
import com.nagp.product.products.Product;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryDelegate inventoryDelegate;

    @GetMapping("/products")
    @CircuitBreaker(name = "userServiceBreaker", fallbackMethod = "getProductsFallback")
    public ResponseEntity<ResponseTO<List<Product>>> getProducts(@RequestParam(value = "category", required = false) String category,
                                                                 @RequestParam(value = "name", required = false) String name,
                                                                 @RequestParam(value = "token") String token){
        log.info("Inside InventoryController :: getProducts() method with category: {} and name: {}", category, name);
        ResponseTO<List<Product>> responseTO = new ResponseTO<>();
        responseTO.setData(inventoryDelegate.getProducts(category, name, token));
        log.info("InventoryController::getProducts ends successfully");
        return ResponseEntity.ok(responseTO);
    }

    @DeleteMapping("/products/{productId}")
    @CircuitBreaker(name = "userServiceBreaker", fallbackMethod = "getProductsFallback")
    public ResponseEntity<ResponseTO<String>> deleteProduct(@PathVariable(value = "productId") String productId,
                                                             @RequestParam(value = "token") String token){
        log.info("Inside InventoryController :: deleteProduct() method with productId: {}", productId);
        ResponseTO<String> responseTO = new ResponseTO<>();
        responseTO.setData(inventoryDelegate.deleteProduct(productId, token));
        log.info("InventoryController::deleteProduct ends successfully");
        return ResponseEntity.ok(responseTO);
    }

    // Internal Api used by Order microservice in placeOrder Api
    @GetMapping("/products/canOrder")
    public ResponseEntity<ResponseTO<String>> canPlaceOrder(@RequestParam(value = "productId") String productId,
                                                             @RequestParam(value = "quantity") String quantity,
                                                             @RequestParam(value = "token") String token){
        log.info("Inside InventoryController :: canPlaceOrder() method with productId: {} and quantity: {}", productId, quantity);
        ResponseTO<String> responseTO = new ResponseTO<>();
        responseTO.setData(inventoryDelegate.canPlaceOrder(productId,quantity , token));
        log.info("InventoryController::canPlaceOrder ends successfully");
        return ResponseEntity.ok(responseTO);
    }

    private <T> ResponseEntity<ResponseTO<T>> getProductsFallback(Exception ex) throws Exception {
        log.error("Fallback triggered for getProducts due to: {}", ex.getMessage(), ex);

        if (ex.getMessage() != null && ex.getMessage().contains("INVALID")) {
            throw ex;
        }
        ResponseTO<T> responseTO = new ResponseTO<>();
        responseTO.setData((T) "User Service is temporarily unavailable. Please try again later.");

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(responseTO);
    }


}
