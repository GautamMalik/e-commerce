package com.nagp.order.utils;

import com.nagp.order.dto.PaymentRequestDTO;
import com.nagp.order.dto.PaymentResponseDTO;
import com.nagp.order.dto.ResponseTO;
import com.nagp.order.exceptions.BadInputException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@Slf4j
public class PaymentUtils {

    @Autowired
    private RestTemplate restTemplate;


    public PaymentResponseDTO processPayment(PaymentRequestDTO paymentDetails, String token) {
        try {
            String baseUrl = "http://PAYMENT-SERVICE/api/payment/process";
            String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                    .queryParam("token", token)
                    .toUriString();
            ResponseEntity<ResponseTO<PaymentResponseDTO>> response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(paymentDetails), new ParameterizedTypeReference<ResponseTO<PaymentResponseDTO>>() {
            });
            if (response == null) {
                log.error("Unable to place order");
                throw new BadInputException("Unable to place order\"");
            }
            if (!response.getStatusCode().is2xxSuccessful()) {
                log.error("Unable to place order");
                throw new BadInputException("Unable to place order");
            }
            return response.getBody().getData();
        } catch (HttpClientErrorException.BadRequest e) {
            if(e.getMessage().contains("INVALID.SESSION")) {
                log.error("Invalid session token: {}", e.getMessage());
                throw new BadInputException("INVALID.SESSION", "invalid session token");
            }
            throw e;
        }
        catch (Exception e) {
            log.error("Error while validating session: {}", e.getMessage());
            throw new BadInputException("Error while validating session",e.getMessage());
        }
    }
}
