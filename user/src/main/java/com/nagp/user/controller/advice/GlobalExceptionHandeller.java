package com.nagp.user.controller.advice;

import com.nagp.user.dto.Error;
import com.nagp.user.dto.ResponseTO;
import com.nagp.user.exceptions.InvalidInputException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandeller {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler({ InvalidInputException.class })
    public ResponseEntity<ResponseTO<?>> handleInvalidInputException(InvalidInputException e) {
        ResponseTO<?> response = new ResponseTO<>();
        response.setErrors(Arrays.asList(new Error(e.exceptionCode(), e.fallbackMessage())));
        response.setError(Boolean.TRUE);
        log.error("InvalidInputException Error {}", e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}