package com.nagp.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaceOrderRequestDTO {

    private String orderId;
    private PaymentRequestDTO paymentDetails;
    private String productId;
}
