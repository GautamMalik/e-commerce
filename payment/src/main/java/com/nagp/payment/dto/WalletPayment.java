package com.nagp.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletPayment extends Payment {
    private String walletId;
    private String walletProvider;

    @Override
    public PaymentResponseDTO processPayment() {
        // Processing wallet payment
        return PaymentResponseDTO.builder()
                .status("Success")
                .message("Wallet payment processed successfully")
                .transactionId("WALLET-" + System.currentTimeMillis())
                .paymentType("Wallet")
                .amount(super.getAmount())
                .date(super.getDate())
                .currency(super.getPaymentCurrency().getCurrency())
                .build();
    }
}
