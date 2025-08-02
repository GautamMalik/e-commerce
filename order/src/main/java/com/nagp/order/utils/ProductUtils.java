package com.nagp.order.utils;

import com.nagp.order.dto.Product;
import com.nagp.order.dto.ResponseTO;
import com.nagp.order.exceptions.BadInputException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Component
@Slf4j
public class ProductUtils {

    @Autowired
    private RestTemplate restTemplate;

    public boolean canplaceOrder(String session, String productId, String quantity){
        try {
            String baseUrl = "http://PRODUCT-SERVICE/api/inventory/products/canOrder";
            String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                    .queryParam("token", session)
                    .queryParam("productId", productId)
                    .queryParam("quantity", quantity)
                    .toUriString();
            ResponseEntity<ResponseTO<String>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<ResponseTO<String>>() {
            });
            if (response == null) {
                log.error("Unable to place order");
                throw new BadInputException("Unable to place order\"");
            }
            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("successfully placed order");
                return true;
            } else {
                log.error("Unable to place order");
                throw new BadInputException("Unable to place order");
            }
        } catch (HttpClientErrorException.BadRequest e) {
            if(e.getMessage().contains("INVALID.SESSION")) {
                log.error("Invalid session token: {}", e.getMessage());
                throw new BadInputException("INVALID.SESSION", "invalid session token");
            } else if (e.getMessage().contains("INVALID.QUANTITY")) {
                log.error("Not enough Quantity for product ID: 102");
                throw new BadInputException("INVALID.QUANTITY", "Not enough Quantity for product ID: "+ productId);
            }
            throw e;
        }
        catch (Exception e) {
            log.error("Error while validating session: {}", e.getMessage());
            throw new BadInputException("Error while validating session",e.getMessage());
        }
    }
}
