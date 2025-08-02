package com.nagp.order.utils;

import com.nagp.order.dto.Product;
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

import java.util.List;

@Component
@Slf4j
public class CartUtils {

    @Autowired
    private RestTemplate restTemplate;

    public List<Product> checkout(String session,String productId){
        try {
            String baseUrl = "http://PRODUCT-SERVICE/api/cart/products/checkout";
            String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                    .queryParam("token", session)
                    .queryParam("productId", productId)
                    .toUriString();
            ResponseEntity<ResponseTO<List<Product>>> response = restTemplate.exchange(url, HttpMethod.POST, null, new ParameterizedTypeReference<ResponseTO<List<Product>>>() {
            });
            if (response == null) {
                log.error("Unable to place order");
                throw new BadInputException("Unable to place order\"");
            }
            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("successfully placed order");
                return response.getBody().getData();
            } else {
                log.error("Unable to place order");
                throw new BadInputException("Unable to place order");
            }
        } catch (HttpClientErrorException.BadRequest e) {
            if(e.getMessage().contains("INVALID.SESSION")) {
                log.error("Invalid session token: {}", e.getMessage());
                throw new BadInputException("INVALID.SESSION", "invalid session token");
            } else if (e.getMessage().contains("No products found for user")) {
                log.error("No products found for user: {}", e.getMessage());
                throw new BadInputException("No products found for user", "No products found for user in cart");
            }
            throw e;
        }
        catch (Exception e) {
            log.error("Error while validating session: {}", e.getMessage());
            throw new BadInputException("Error while validating session",e.getMessage());
        }
    }

    public void deleteCart(String session){
        try {
            String baseUrl = "http://PRODUCT-SERVICE/api/cart/products/checkout";
            String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                    .queryParam("token", session)
                    .toUriString();
            ResponseEntity<ResponseTO<String>> response = restTemplate.exchange(url, HttpMethod.DELETE, null, new ParameterizedTypeReference<ResponseTO<String>>() {
            });
            if (response == null) {
                log.error("Unable to place order");
                throw new BadInputException("Unable to place order\"");
            }
            if (!response.getStatusCode().is2xxSuccessful()) {
                log.error("Unable to place order");
                throw new BadInputException("Unable to place order");
            }
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
