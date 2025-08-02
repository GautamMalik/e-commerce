package com.nagp.payment.dto;

import com.nagp.payment.enums.PaymentTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentContext {

    private Payment payment;

    public PaymentResponseDTO executePayment(String paymentType, Map<String, String> data) {
        PaymentTypeEnum type = PaymentTypeEnum.valueOf(paymentType.toUpperCase());
        this.payment = PaymentFactory.createPayment(type, data);
        return this.payment.processPayment();
    }

}
