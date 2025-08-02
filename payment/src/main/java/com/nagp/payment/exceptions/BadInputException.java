package com.nagp.payment.exceptions;

public class BadInputException extends InvalidInputException {
    public BadInputException(String exceptionCode) {
        super(exceptionCode);
    }

    public BadInputException(String exceptionCode, Throwable cause) {
        super(exceptionCode, cause);
    }

    public BadInputException(String exceptionCode, String fallbackMessage) {
        super(exceptionCode, fallbackMessage);
    }

    public BadInputException(String exceptionCode, String fallbackMessage, Throwable cause) {
        super(exceptionCode, fallbackMessage, cause);
    }
}
