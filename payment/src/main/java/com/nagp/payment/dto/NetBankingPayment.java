package com.nagp.payment.dto;

import lombok.Data;

@Data
public class NetBankingPayment extends Payment {
    private String bankName;
    private String accountNumber;
    private String ifscCode;

    @Override
    public PaymentResponseDTO processPayment() {
        // Processing net banking payment
        return PaymentResponseDTO.builder()
                .status("Success")
                .message("Net banking payment of " + super.getAmount() + " processed successfully using account number: " + accountNumber)
                .transactionId("NETBANK-" + System.currentTimeMillis())
                .paymentType("Net Banking")
                .amount(super.getAmount())
                .date(super.getDate())
                .currency(super.getPaymentCurrency().name())
                .build();
    }
}
