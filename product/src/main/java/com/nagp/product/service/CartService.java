package com.nagp.product.service;

import com.nagp.product.dto.CartRequestDTO;
import com.nagp.product.products.Product;

import java.util.List;

public interface CartService {
    List<Product> getProducts(Integer userId, String category, String name, Integer id);

    String removeProductFromCart(Integer userId, String productId);

    String clearCart(Integer userId);

    String addProduct(CartRequestDTO cartRequestDTO);

    String deleteProduct(Integer userId, String productId);
}
