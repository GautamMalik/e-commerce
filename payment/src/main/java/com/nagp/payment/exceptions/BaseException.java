package com.nagp.payment.exceptions;

public interface BaseException {
    String exceptionCode();

    ErrorCode errorCode();

    String fallbackMessage();
}
