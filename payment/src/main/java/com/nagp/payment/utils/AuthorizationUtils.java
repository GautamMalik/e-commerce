package com.nagp.payment.utils;


import com.nagp.payment.dto.ResponseTO;
import com.nagp.payment.exceptions.BadInputException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class AuthorizationUtils {

    @Autowired
    private RestTemplate restTemplate;

    public boolean isValidSession(String session){
        try {
            String url = "http://USER-SERVICE/api/user/is-valid-token?token=" + session;
            ResponseEntity<ResponseTO<String>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<ResponseTO<String>>() {
            });
            if (response == null) {
                log.error("Unable to validate session");
                throw new BadInputException("Unable to validate session");
            }
            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Session is valid");
                return true;
            } else {
                log.error("Session is invalid");
                throw new BadInputException("Session is invalid");
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
