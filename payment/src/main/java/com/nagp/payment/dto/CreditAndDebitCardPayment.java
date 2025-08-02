package com.nagp.payment.dto;

import lombok.Data;

@Data
public class CreditAndDebitCardPayment extends Payment {
    private String cardNumber;
    private String cardHolderName;
    private String expiryDate;
    private String cvv;

    @Override
    public PaymentResponseDTO processPayment() {
        // Processing card payment
        return PaymentResponseDTO.builder()
                .status("Success")
                .message("Credit/Debit card payment processed successfully")
                .transactionId("CARD-" + System.currentTimeMillis())
                .paymentType("Credit/Debit Card")
                .amount(super.amount)
                .date(super.date)
                .currency(super.paymentCurrency.name())
                .build();
    }
}
