package com.nagp.user.exceptions;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    String errorCode();

    HttpStatus httpStatus();
}
