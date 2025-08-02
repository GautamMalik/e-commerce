package com.nagp.product.exceptions;

public interface BaseException {
    String exceptionCode();

    ErrorCode errorCode();

    String fallbackMessage();
}
