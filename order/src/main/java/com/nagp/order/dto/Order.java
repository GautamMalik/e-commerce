package com.nagp.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {
    private String orderId;
    private String status;
    private String paymentType;
    private String orderDate;
    private int productId;
    private String paymentId;
    private String paymentDate;
    private String productName;
    private String productDescription;
    private double price;
    private String quantity;
}
