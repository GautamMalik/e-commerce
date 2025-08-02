package com.nagp.payment.exceptions;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    String errorCode();

    HttpStatus httpStatus();
}
