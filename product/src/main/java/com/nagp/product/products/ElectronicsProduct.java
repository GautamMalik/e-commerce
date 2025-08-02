package com.nagp.product.products;

import com.nagp.product.domain.ProductEntity;
import com.nagp.product.enums.ProductEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ElectronicsProduct extends ProductEntity implements Product, Serializable {
    private String model;
    private String warranty;
    private String powerConsumption;

    @Override
    public String getCategory() {
        return ProductEnum.ELECTRONICS.getCategory();
    }


}
