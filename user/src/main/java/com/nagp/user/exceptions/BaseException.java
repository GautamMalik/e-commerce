package com.nagp.user.exceptions;

public interface BaseException {
    String exceptionCode();

    ErrorCode errorCode();

    String fallbackMessage();
}
