package com.nagp.payment.dto;

import com.nagp.payment.enums.PaymentCurrencyEnum;
import lombok.Data;

@Data
public abstract class Payment {

    protected String amount;
    protected String date;
    protected PaymentCurrencyEnum paymentCurrency;
    protected abstract PaymentResponseDTO processPayment();
}
