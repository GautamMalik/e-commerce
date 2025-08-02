package com.nagp.product.service.impl;

import com.nagp.product.contstants.Constants;
import com.nagp.product.dto.CartRequestDTO;
import com.nagp.product.exceptions.BadInputException;
import com.nagp.product.products.ElectronicsProduct;
import com.nagp.product.products.FashionProduct;
import com.nagp.product.products.Product;
import com.nagp.product.products.SportsProduct;
import com.nagp.product.service.CartService;
import com.nagp.product.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private Map<Integer,List<Product>> cartProducts = new HashMap<>();
    private final InventoryService inventoryService;



    @Override
    public List<Product> getProducts(Integer userId, String category, String name, Integer id) {
        log.info("Inside CartServiceImpl :: getProducts() method with userId: {}", userId);
        if (cartProducts.get(userId) == null || cartProducts.get(userId).isEmpty()) {
            log.error("No products found for user ID: {}", userId);
            return new ArrayList<>();
        }

        List<Product> prod;
        if (id != null) {
            prod = cartProducts.get(userId).stream()
                    .filter(product -> {
                        if (product instanceof SportsProduct sportsProduct) {
                            return sportsProduct.getId() == id;
                        } else if (product instanceof FashionProduct fashionProduct) {
                            return fashionProduct.getId() == id;
                        } else if (product instanceof ElectronicsProduct electronicsProduct) {
                            return electronicsProduct.getId() == id;
                        }
                        return false;
                    })
                    .toList();
        } else if (StringUtils.isEmpty(category) && StringUtils.isEmpty(name))
            prod = cartProducts.get(userId);
        else if (!StringUtils.isEmpty(category) && StringUtils.isEmpty(name)) {
            prod = cartProducts.get(userId).stream()
                    .filter(product -> product.getCategory().equalsIgnoreCase(category))
                    .toList();
        } else {
            prod = cartProducts.get(userId).stream()
                    .filter(product -> {
                        if (product instanceof com.nagp.product.products.SportsProduct sportsProduct) {
                            return sportsProduct.getName().equalsIgnoreCase(name);
                        } else if (product instanceof com.nagp.product.products.FashionProduct fashionProduct) {
                            return fashionProduct.getName().equalsIgnoreCase(name);
                        } else if (product instanceof com.nagp.product.products.ElectronicsProduct electronicsProduct) {
                            return electronicsProduct.getName().equalsIgnoreCase(name);
                        }
                        return false;
                    })
                    .toList();
        }

        return new ArrayList<>(prod);
    }

    @Override
    public String addProduct(CartRequestDTO cartRequestDTO) {
        log.info("Inside CartServiceImpl :: addProduct() method with cartRequestDTO: {}", cartRequestDTO);
        int quantity = 0;
        cartProducts.putIfAbsent(cartRequestDTO.getUserId(), new ArrayList<>());
        canAddProduct(cartRequestDTO);
        List<Product> productList = inventoryService.getProducts(null, null, cartRequestDTO.getProductId());
        if (productList.isEmpty()) {
            log.error("Product with ID {} not found", cartRequestDTO.getProductId());
            throw new BadInputException(Constants.INVALID_PRODUCT_ID, "Product with ID " + cartRequestDTO.getProductId() + " not found");
        }
        Product product = (Product) SerializationUtils.clone((Serializable) productList.get(0));
        if(product instanceof SportsProduct sportsProduct && sportsProduct.getId() == cartRequestDTO.getProductId()) {
            quantity = sportsProduct.getQuantity();
        } else if(product instanceof FashionProduct fashionProduct && fashionProduct.getId() == cartRequestDTO.getProductId()) {
            quantity = fashionProduct.getQuantity();
        } else if(product instanceof ElectronicsProduct electronicsProduct && electronicsProduct.getId() == cartRequestDTO.getProductId()) {
            quantity = electronicsProduct.getQuantity();
            electronicsProduct.setQuantity(cartRequestDTO.getQuantity());
        }

        List<Product> userProducts = cartProducts.get(cartRequestDTO.getUserId());
        if(userProducts.isEmpty()){
            if(product instanceof SportsProduct sportsProduct && sportsProduct.getId() == cartRequestDTO.getProductId()) {
                sportsProduct.setQuantity(cartRequestDTO.getQuantity());
            } else if(product instanceof FashionProduct fashionProduct && fashionProduct.getId() == cartRequestDTO.getProductId()) {
                fashionProduct.setQuantity(cartRequestDTO.getQuantity());
            } else if(product instanceof ElectronicsProduct electronicsProduct && electronicsProduct.getId() == cartRequestDTO.getProductId()) {
                electronicsProduct.setQuantity(cartRequestDTO.getQuantity());
            }
            cartProducts.get(cartRequestDTO.getUserId()).add(product);
            return "Product added to cart successfully";
        }

        boolean productFound = false;

        for(Product prod : userProducts) {
            if(prod instanceof SportsProduct sportsProduct && sportsProduct.getId() == cartRequestDTO.getProductId()) {
                if(sportsProduct.getQuantity() + cartRequestDTO.getQuantity() > quantity) {
                    log.error("Quantity exceeds available stock");
                    throw new BadInputException(Constants.INVALID_QUANTITY, "Quantity exceeds available stock");
                }
                sportsProduct.setQuantity(sportsProduct.getQuantity() + cartRequestDTO.getQuantity());
                productFound = true;
            } else if(prod instanceof FashionProduct fashionProduct && fashionProduct.getId() == cartRequestDTO.getProductId()) {
                if(fashionProduct.getQuantity() + cartRequestDTO.getQuantity() > quantity) {
                    log.error("Quantity exceeds available stock");
                    throw new BadInputException(Constants.INVALID_QUANTITY, "Quantity exceeds available stock");
                }
                fashionProduct.setQuantity(fashionProduct.getQuantity() + cartRequestDTO.getQuantity());
                productFound = true;
            } else if(prod instanceof ElectronicsProduct electronicsProduct && electronicsProduct.getId() == cartRequestDTO.getProductId()) {
                if(electronicsProduct.getQuantity() + cartRequestDTO.getQuantity() > quantity) {
                    log.error("Quantity exceeds available stock");
                    throw new BadInputException(Constants.INVALID_QUANTITY, "Quantity exceeds available stock");
                }
                electronicsProduct.setQuantity(electronicsProduct.getQuantity() + cartRequestDTO.getQuantity());
                productFound = true;
            }
        }
        if (!productFound) {
            if(product instanceof SportsProduct sportsProduct) {
                sportsProduct.setQuantity(cartRequestDTO.getQuantity());
            } else if(product instanceof FashionProduct fashionProduct) {
                fashionProduct.setQuantity(cartRequestDTO.getQuantity());
            } else if(product instanceof ElectronicsProduct electronicsProduct) {
                electronicsProduct.setQuantity(cartRequestDTO.getQuantity());
            }
            userProducts.add(product);
        }
        cartProducts.put(cartRequestDTO.getUserId(), userProducts);
        return "Product added to cart successfully";
    }

    @Override
    public String deleteProduct(Integer userId, String productId) {
        log.info("Inside CartServiceImpl :: deleteProduct() method with userId: {} and productId: {}", userId, productId);

        if(StringUtils.isEmpty(productId)) {
            cartProducts.get(userId).clear();
            return "Cart cleared successfully";
        }

        if (cartProducts.get(userId).stream().noneMatch(product -> {
            if (product instanceof SportsProduct sportsProduct && sportsProduct.getId() == Integer.parseInt(productId)) {
                return true;
            } else if (product instanceof FashionProduct fashionProduct && fashionProduct.getId() == Integer.parseInt(productId)) {
                return true;
            } else if (product instanceof ElectronicsProduct electronicsProduct && electronicsProduct.getId() == Integer.parseInt(productId)) {
                return true;
            }
            return false;
        })) {
            log.error("Product with ID {} not found", productId);
            throw new BadInputException(Constants.INVALID_PRODUCT_ID, "Product with ID " + productId + " not found");
        }

        cartProducts.get(userId).removeIf(product -> {
            if (product instanceof SportsProduct sportsProduct) {
                return sportsProduct.getId() == Integer.parseInt(productId);
            } else if (product instanceof FashionProduct fashionProduct) {
                return fashionProduct.getId() == Integer.parseInt(productId);
            } else if (product instanceof ElectronicsProduct electronicsProduct) {
                return electronicsProduct.getId() == Integer.parseInt(productId);
            }
            return false;
        });
        return "Product deleted successfully";
    }

    private boolean canAddProduct(CartRequestDTO cartRequestDTO) {
        return inventoryService.canAddProductInCart(cartRequestDTO);
    }

    @Override
    public String removeProductFromCart(Integer userId, String productId) {
        return null;
    }

    @Override
    public String clearCart(Integer userId) {
        return null;
    }
}
