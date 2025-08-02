package com.nagp.payment.service;

import com.nagp.payment.dto.PaymentRequestDTO;
import com.nagp.payment.dto.PaymentResponseDTO;

public interface PaymentService {
    PaymentResponseDTO processPayment(PaymentRequestDTO requestDTO);

}
