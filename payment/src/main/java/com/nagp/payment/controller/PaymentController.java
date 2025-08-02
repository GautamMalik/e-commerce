package com.nagp.payment.controller;

import com.nagp.payment.delegate.PaymentDelegate;
import com.nagp.payment.dto.PaymentRequestDTO;
import com.nagp.payment.dto.PaymentResponseDTO;
import com.nagp.payment.dto.ResponseTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentDelegate paymentDelegate;

    @PostMapping("/process")
    @CircuitBreaker(name = "userServiceBreaker", fallbackMethod = "getProductsFallback")
    public ResponseEntity<ResponseTO<PaymentResponseDTO>> processPayment(@RequestBody PaymentRequestDTO paymentDetails,
                                                             @RequestParam(value = "token") String token) {
        log.info("Inside PaymentController :: processPayment() method with paymentDetails: {}", paymentDetails);
        ResponseTO<PaymentResponseDTO> responseTO = new ResponseTO<>();
        responseTO.setData(paymentDelegate.processPayment(paymentDetails,token));
        log.info("PaymentController::processPayment ends successfully");
        return ResponseEntity.ok(responseTO);
    }

    private <T> ResponseEntity<ResponseTO<T>> getProductsFallback(Exception ex) throws Exception {
        log.error("Fallback triggered for getProducts due to: {}", ex.getMessage(), ex);

        if (ex.getMessage() != null && ex.getMessage().contains("INVALID.SESSION")) {
            throw ex;
        }
        ResponseTO<T> responseTO = new ResponseTO<>();
        responseTO.setData((T) "User Service is temporarily unavailable. Please try again later.");

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(responseTO);
    }

}
