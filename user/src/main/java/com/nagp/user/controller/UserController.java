package com.nagp.user.controller;

import com.nagp.user.delegate.UserDelegate;
import com.nagp.user.dto.CreateNewUserRequestDTO;
import com.nagp.user.dto.ResponseTO;
import com.nagp.user.exceptions.BadInputException;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/")
public class UserController {

    @Autowired
    private UserDelegate userDelegate;

    @PostMapping("/user/register")
    @RateLimiter(name = "userServiceRateLimiter", fallbackMethod = "userServiceFallback")
    public ResponseEntity<ResponseTO<String>> createNewUser(@RequestBody CreateNewUserRequestDTO createNewUserRequestDTO) {
        log.info("Inside UserController :: createNewUser() method, request: {}", createNewUserRequestDTO);
        ResponseTO<String> responseTO = new ResponseTO<>();
        responseTO.setData(userDelegate.createNewUser(createNewUserRequestDTO));
        log.info("UserController::createNewUser ends successfully");
        return ResponseEntity.ok(responseTO);
    }

    @GetMapping("/user/login")
    @RateLimiter(name = "userServiceRateLimiter", fallbackMethod = "userServiceFallback")
    public ResponseEntity<ResponseTO<String>> loginUser(@RequestParam(name = "email") String email, @RequestParam(name = "password") String password) {
        log.info("Inside UserController :: loginUser() method, username: {}", email);
        ResponseTO<String> responseTO = new ResponseTO<>();
        responseTO.setData(userDelegate.loginUser(email, password));
        log.info("UserController::loginUser ends successfully");
        return ResponseEntity.ok(responseTO);
    }

    @GetMapping("/user/logout")
    @RateLimiter(name = "userServiceRateLimiter", fallbackMethod = "userServiceFallback")
    public ResponseEntity<ResponseTO<String>> logoutUser(@RequestParam(name = "email") String email, @RequestParam(name = "token") String token) {
        log.info("Inside UserController :: logoutUser() method, username: {}", email);
        ResponseTO<String> responseTO = new ResponseTO<>();
        responseTO.setData(userDelegate.logoutUser(email,token));
        log.info("UserController::logoutUser ends successfully");
        return ResponseEntity.ok(responseTO);
    }

    @GetMapping("/user/forgot-password")
    @RateLimiter(name = "userServiceRateLimiter", fallbackMethod = "userServiceFallback")
    public ResponseEntity<ResponseTO<String>> forgotPassword(@RequestParam(name = "email") String email) {
        log.info("Inside UserController :: forgotPassword() method, username: {}", email);
        ResponseTO<String> responseTO = new ResponseTO<>();
        responseTO.setData(userDelegate.forgotPassword(email));
        log.info("UserController::forgotPassword ends successfully");
        return ResponseEntity.ok(responseTO);
    }

    @GetMapping("/user/is-valid-token")
    @RateLimiter(name = "userServiceRateLimiter", fallbackMethod = "userServiceFallback")
    public ResponseEntity<ResponseTO<String>> isValidToken(@RequestParam(name = "token") String token) {
        ResponseTO<String> responseTO = new ResponseTO<>();
        responseTO.setData(userDelegate.isValidToken(token));
        log.info("UserController::isValidToken ends successfully");
        return ResponseEntity.ok(responseTO);
    }

    private <T> ResponseEntity<ResponseTO<T>> userServiceFallback(Exception ex) throws Exception {
        log.error("Fallback triggered due to: {}", ex.getMessage(), ex);
        if(ex!=null && ex instanceof BadInputException)
            throw ex;
        ResponseTO<T> responseTO = new ResponseTO<>();
        responseTO.setData((T) "Service is currently unavailable due to load. Please try again later.");

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(responseTO);
    }
}
