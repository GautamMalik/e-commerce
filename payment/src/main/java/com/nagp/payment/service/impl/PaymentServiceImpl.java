package com.nagp.payment.service.impl;

import com.nagp.payment.constants.Constants;
import com.nagp.payment.dto.*;
import com.nagp.payment.enums.PaymentTypeEnum;
import com.nagp.payment.exceptions.BadInputException;
import com.nagp.payment.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    @Override
    public PaymentResponseDTO processPayment(PaymentRequestDTO requestDTO) {
        PaymentContext paymentContext = new PaymentContext();

        Map<String,String> data = new HashMap<>();
        data.put("amount", String.valueOf(requestDTO.getAmount()));
        data.put("date", String.valueOf(LocalDateTime.now()));
        data.put("paymentCurrency", requestDTO.getPaymentCurrency());
        PaymentTypeEnum type = PaymentTypeEnum.valueOf(requestDTO.getMethod().toUpperCase());
        addStregyWiseData(requestDTO, data, type);

        return paymentContext.executePayment(requestDTO.getMethod(), data);
    }

    private static void addStregyWiseData(PaymentRequestDTO requestDTO, Map<String, String> data, PaymentTypeEnum type) {
        switch (type) {
            case UPI -> {
                data.put("upiId", requestDTO.getUpiId());
                data.put("upiPin", requestDTO.getUpiPin());
            }
            case WALLET -> {
                data.put("walletId", requestDTO.getWalletId());
                data.put("walletProvider", requestDTO.getWalletProvider());
            }
            case CREDIT_CARD, DEBIT_CARD -> {
                data.put("cardNumber", requestDTO.getCardNumber());
                data.put("cardHolderName", requestDTO.getCardHolderName());
                data.put("cvv", requestDTO.getCvv());
                data.put("expiryDate", String.valueOf(requestDTO.getExpiryDate()));
            }
            case NET_BANKING -> {
                data.put("bankName", requestDTO.getBankName());
                data.put("accountNumber", requestDTO.getAccountNumber());
                data.put("ifscCode", requestDTO.getIfscCode());
            }
            default -> throw new BadInputException(Constants.INVALID_PAYMENT_TYPE, "Unsupported payment type: " + type);
        }
    }
}
