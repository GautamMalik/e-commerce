package com.nagp.product.controller;


import com.nagp.product.delegate.CartDelegate;
import com.nagp.product.dto.CartRequestDTO;
import com.nagp.product.dto.ResponseTO;
import com.nagp.product.products.Product;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/cart")
public class CartController {

    private final CartDelegate cartDelegate;

    @GetMapping("/products")
    @CircuitBreaker(name = "userServiceBreaker", fallbackMethod = "getProductsFallback")
    public ResponseEntity<ResponseTO<List<Product>>> getProducts(@RequestParam(value = "token") String token,
                                                                 @RequestParam(value = "category", required = false) String category,
                                                                 @RequestParam(value = "id", required = false) Integer id,
                                                                 @RequestParam(value = "name", required = false) String name){
        log.info("Inside CartController :: getProducts() method with category: {} and name: {} and id : {}", category, name, id);
        ResponseTO<List<Product>> responseTO = new ResponseTO<>();
        responseTO.setData(cartDelegate.getProducts(token, category, name, id));
        log.info("CartController::getProducts ends successfully");
        return ResponseEntity.ok(responseTO);
    }

    @PostMapping("/products")
    @CircuitBreaker(name = "userServiceBreaker", fallbackMethod = "getProductsFallback")
    public ResponseEntity<ResponseTO<String>> addProduct(@RequestBody CartRequestDTO cartRequestDTO,
                                                         @RequestParam(value = "token") String token){
        log.info("Inside CartController :: addProduct() method with cartRequestDTO: {}", cartRequestDTO);
        ResponseTO<String> responseTO = new ResponseTO<>();
        responseTO.setData(cartDelegate.addProduct(cartRequestDTO, token));
        log.info("CartController::addProduct ends successfully");
        return ResponseEntity.ok(responseTO);
    }

    @DeleteMapping("/products")
    @CircuitBreaker(name = "userServiceBreaker", fallbackMethod = "getProductsFallback")
    public ResponseEntity<ResponseTO<String>> deleteProduct(@RequestParam(value = "token") String token){
        log.info("Inside CartController :: deleteProduct() method");
        ResponseTO<String> responseTO = new ResponseTO<>();
        responseTO.setData(cartDelegate.deleteProduct(token));
        log.info("CartController::deleteProduct ends successfully");
        return ResponseEntity.ok(responseTO);
    }

    // Internal API used in Order microservice in processOrder Api
    @PostMapping("/products/checkout")
    public ResponseEntity<ResponseTO<List<Product>>> checkout(@RequestParam(value = "productId", required = false) String productId, @RequestParam(value = "token") String token){
        log.info("Inside CartController :: checkout() method");
        ResponseTO<List<Product>> responseTO = new ResponseTO<>();
        responseTO.setData(cartDelegate.checkout(productId,token));
        log.info("CartController::checkout ends successfully");
        return ResponseEntity.ok(responseTO);
    }

    private <T> ResponseEntity<ResponseTO<T>> getProductsFallback(Exception ex) throws Exception {
        log.error("Fallback triggered for getProducts due to: {}", ex.getMessage(), ex);

        if (ex.getMessage() != null && (ex.getMessage().contains("INVALID"))) {
            throw ex;
        }
        ResponseTO<T> responseTO = new ResponseTO<>();
        responseTO.setData((T) "User Service is temporarily unavailable. Please try again later.");

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(responseTO);
    }
}
