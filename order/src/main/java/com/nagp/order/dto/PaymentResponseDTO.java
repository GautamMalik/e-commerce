package com.nagp.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentResponseDTO {
    private int userId;
    private String status;
    private String message;
    private String transactionId;
    private String paymentType;
    private String amount;
    private String date;
    private String currency;
}
