package com.nagp.product.service;

import com.nagp.product.dto.CartRequestDTO;
import com.nagp.product.products.Product;

import java.util.List;

public interface InventoryService {
    List<Product> getProducts(String category, String name, Integer productId);

    String deleteProduct(String productId);

    boolean canOrderProduct(CartRequestDTO cartRequestDTO);

    boolean canAddProductInCart(CartRequestDTO cartRequestDTO);
}
