package com.nagp.product.service.impl;

import com.nagp.product.contstants.Constants;
import com.nagp.product.dto.CartRequestDTO;
import com.nagp.product.enums.ProductEnum;
import com.nagp.product.exceptions.BadInputException;
import com.nagp.product.products.*;
import com.nagp.product.service.InventoryService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class InventoryServiceImpl implements InventoryService {

    private List<Product> products = new ArrayList<>();

    @Override
    public List<Product> getProducts(String category, String name, Integer productId) {

        log.info("Inside InventoryServiceImpl :: getProducts() method with category: {} and name: {}", category, name);

        if( productId != null) {
            return products.stream()
                    .filter(product -> {
                        if (product instanceof SportsProduct sportsProduct) {
                            return sportsProduct.getId()==productId;
                        } else if (product instanceof FashionProduct fashionProduct) {
                            return fashionProduct.getId()==productId;
                        } else if (product instanceof ElectronicsProduct electronicsProduct) {
                            return electronicsProduct.getId()==productId;
                        }
                        return false;
                    })
                    .toList();
        } else if(StringUtils.isEmpty(category) && StringUtils.isEmpty(name))
            return products;
        else if (!StringUtils.isEmpty(category) && StringUtils.isEmpty(name)) {
            return products.stream()
                    .filter(product -> product.getCategory().equalsIgnoreCase(category))
                    .toList();
        } else {
            return products.stream()
                    .filter(product -> {
                        if (product instanceof SportsProduct sportsProduct) {
                            return sportsProduct.getName().equalsIgnoreCase(name);
                        } else if (product instanceof FashionProduct fashionProduct) {
                            return fashionProduct.getName().equalsIgnoreCase(name);
                        } else if (product instanceof ElectronicsProduct electronicsProduct) {
                            return electronicsProduct.getName().equalsIgnoreCase(name);
                        }
                        return false;
                    })
                    .toList();
        }
    }

    @Override
    public String deleteProduct(String productId) {
        log.info("Inside InventoryServiceImpl :: deleteProduct() method with productId: {}", productId);

        if (products.stream().noneMatch(product -> {
            if (product instanceof SportsProduct sportsProduct) {
                return sportsProduct.getId() == Integer.parseInt(productId);
            } else if (product instanceof FashionProduct fashionProduct) {
                return fashionProduct.getId() == Integer.parseInt(productId);
            } else if (product instanceof ElectronicsProduct electronicsProduct) {
                return electronicsProduct.getId() == Integer.parseInt(productId);
            }
            return false;
        })) {
            log.error("Product with ID {} not found", productId);
            throw new BadInputException(Constants.INVALID_PRODUCT_ID, "Product with ID " + productId + " not found");
        }

        products.removeIf(product -> {
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

    @Override
    public boolean canOrderProduct(CartRequestDTO cartRequestDTO) {
        log.info("Inside InventoryServiceImpl :: canAddProduct() method with cartRequestDTO : {}", cartRequestDTO);

        for (Product product : products) {
            if (matchesProduct(product, cartRequestDTO)) {
                int availableQuantity = getQuantity(product);
                if (availableQuantity >= cartRequestDTO.getQuantity()) {
                    reduceQuantity(product, cartRequestDTO.getQuantity());
                    return true;
                } else {
                    log.error("Not enough Quantity for product ID: {}", cartRequestDTO.getProductId());
                    throw new BadInputException(Constants.INVALID_QUANTITY,
                            "Not enough Quantity for product ID: " + cartRequestDTO.getProductId());
                }
            }
        }

        throw new BadInputException(Constants.INVALID_PRODUCT_ID,
                "Product with ID " + cartRequestDTO.getProductId() + " not found");
    }

    @Override
    public boolean canAddProductInCart(CartRequestDTO cartRequestDTO) {
        for(Product product : products) {
            if (matchesProduct(product, cartRequestDTO)) {
                int availableQuantity = getQuantity(product);
                if (availableQuantity >= cartRequestDTO.getQuantity()) {
                    return true;
                } else {
                    log.error("Not enough Quantity for product ID: {}", cartRequestDTO.getProductId());
                    throw new BadInputException(Constants.INVALID_QUANTITY,
                            "Not enough available quantity for product ID: " + cartRequestDTO.getProductId());
                }
            }
        }
        return false;
    }



    private void reduceQuantity(Product product, int quantity) {
        if (product instanceof SportsProduct sportsProduct) {
            sportsProduct.setQuantity(sportsProduct.getQuantity() - quantity);
        } else if (product instanceof FashionProduct fashionProduct) {
            fashionProduct.setQuantity(fashionProduct.getQuantity() - quantity);
        } else if (product instanceof ElectronicsProduct electronicsProduct) {
            electronicsProduct.setQuantity(electronicsProduct.getQuantity() - quantity);
        }
    }

    private boolean matchesProduct(Product product, CartRequestDTO dto) {
        if (product instanceof SportsProduct sportsProduct) {
            return sportsProduct.getId() == dto.getProductId();
        } else if (product instanceof FashionProduct fashionProduct) {
            return fashionProduct.getId() == dto.getProductId();
        } else if (product instanceof ElectronicsProduct electronicsProduct) {
            return electronicsProduct.getId() == dto.getProductId();
        }
        return false;
    }

    private int getQuantity(Product product) {
        if (product instanceof SportsProduct sportsProduct) {
            return sportsProduct.getQuantity();
        } else if (product instanceof FashionProduct fashionProduct) {
            return fashionProduct.getQuantity();
        } else if (product instanceof ElectronicsProduct electronicsProduct) {
            return electronicsProduct.getQuantity();
        }
        return 0;
    }


    @PostConstruct
    public void addProduct() {
        for (int i = 1; i <= 4; i++) {
            SportsProduct sportsProduct = ((SportsProduct) ProductFactory.createProduct(ProductEnum.SPORTS.toString()));
            sportsProduct.setId(100 + i);
            sportsProduct.setName("Sports Product " + i);
            sportsProduct.setDescription("Description for Sports Product " + i);
            sportsProduct.setPrice(1000.00 + i * 100);
            sportsProduct.setSportType("Sport Type " + i);
            sportsProduct.setPlayerLevel("Level " + i);
            sportsProduct.setRating("4.0");
            sportsProduct.setMaterial("Material " + i);
            sportsProduct.setBrand("Brand " + i);
            sportsProduct.setImageUrl("http://example.com/sports-product-" + i + ".jpg");
            sportsProduct.setQuantity(i);
            products.add(sportsProduct);
        }

        for (int i = 1; i <= 4; i++) {
            FashionProduct fashionProduct = ((FashionProduct) ProductFactory.createProduct(ProductEnum.FASHION.toString()));
            fashionProduct.setId(200 + i);
            fashionProduct.setName("Fashion Product " + i);
            fashionProduct.setDescription("Description for Fashion Product " + i);
            fashionProduct.setPrice(500.00 + i * 50);
            fashionProduct.setSize("Size " + i);
            fashionProduct.setColor("Color " + i);
            fashionProduct.setRating("4.0");
            fashionProduct.setFabricType("Fabric Type " + i);
            fashionProduct.setGender(i % 2 == 0 ? "Unisex" : "Male");
            fashionProduct.setBrand("Brand " + i);
            fashionProduct.setImageUrl("http://example.com/fashion-product-" + i + ".jpg");
            fashionProduct.setQuantity(i);
            products.add(fashionProduct);
        }

        for (int i = 1; i <= 4; i++) {
            ElectronicsProduct electronicsProduct = (ElectronicsProduct) ProductFactory.createProduct(ProductEnum.ELECTRONICS.toString());
            electronicsProduct.setId(300 + i);
            electronicsProduct.setName("Electronics Product " + i);
            electronicsProduct.setDescription("Description for Electronics Product " + i);
            electronicsProduct.setPrice(2000.00 + i * 200);
            electronicsProduct.setRating("4.0");
            electronicsProduct.setWarranty("Warranty " + i);
            electronicsProduct.setPowerConsumption("Power " + i);
            electronicsProduct.setModel("Model " + i);
            electronicsProduct.setBrand("Brand " + i);
            electronicsProduct.setImageUrl("http://example.com/electronics-product-" + i + ".jpg");
            electronicsProduct.setQuantity(i);
            products.add(electronicsProduct);
        }
    }
}
