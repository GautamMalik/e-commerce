package com.nagp.product.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductEntity implements  Serializable {

    private int id;
    private String name;
    private String description;
    private double price;
    private String brand;
    private String imageUrl;
    private String rating;
    private int quantity;
}
