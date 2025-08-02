package com.nagp.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpiPaymnet extends Payment{
    private String upiId;
    private String upiPin;


    @Override
    public PaymentResponseDTO processPayment() {
        // Processing UPI payment
        return PaymentResponseDTO.builder()
                .status("Success")
                .message("UPI payment of " + super.getAmount() + " processed successfully using UPI ID: " + upiId)
                .transactionId("UPI-" + System.currentTimeMillis())
                .paymentType("UPI")
                .amount(super.getAmount())
                .date(super.getDate())
                .currency(super.getPaymentCurrency().name())
                .build();
    }
}
