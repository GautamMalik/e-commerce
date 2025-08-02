package com.nagp.order.exceptions;

public interface BaseException {
    String exceptionCode();

    ErrorCode errorCode();

    String fallbackMessage();
}
