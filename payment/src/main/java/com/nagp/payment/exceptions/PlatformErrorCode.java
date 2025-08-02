package com.nagp.payment.exceptions;

import org.springframework.http.HttpStatus;

public enum PlatformErrorCode implements ErrorCode {
    INVALID_INPUT("INVALID_INPUT", HttpStatus.BAD_REQUEST),
    DUPLICATE_RECORD("DUPLICATE_RECORD", HttpStatus.CONFLICT),
    SERVER_CONFLICT("SERVER_CONFLICT", HttpStatus.CONFLICT),
    RECORD_NOT_FOUND("RECORD_NOT_FOUND", HttpStatus.NOT_FOUND),
    UNAUTHORIZED("UNAUTHORIZED_ACCESS", HttpStatus.UNAUTHORIZED),
    SERVER_ERROR("SERVER_ERROR", HttpStatus.INTERNAL_SERVER_ERROR);

    private String errorCode;
    private HttpStatus httpStatus;

    private PlatformErrorCode(String errorCode, HttpStatus httpStatus) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    public String errorCode() {
        return this.errorCode;
    }

    public HttpStatus httpStatus() {
        return this.httpStatus;
    }
}
