package com.nagp.product.products;

import com.nagp.product.domain.ProductEntity;
import com.nagp.product.enums.ProductEnum;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FashionProduct extends ProductEntity implements Product, Serializable {

    private String size;
    private String color;
    private String fabricType;
    private String gender;
    @Override
    public String getCategory() {
        return ProductEnum.FASHION.getCategory();
    }
}
