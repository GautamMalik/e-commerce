package com.nagp.user.exceptions;

public abstract class InvalidInputException extends BaseUncheckedException {
    public InvalidInputException(String exceptionCode) {
        super(exceptionCode, PlatformErrorCode.INVALID_INPUT);
    }

    public InvalidInputException(String exceptionCode, Throwable cause) {
        super(exceptionCode, cause, PlatformErrorCode.INVALID_INPUT);
    }

    public InvalidInputException(String exceptionCode, String fallbackMessage) {
        super(exceptionCode, fallbackMessage, PlatformErrorCode.INVALID_INPUT);
    }

    public InvalidInputException(String exceptionCode, String fallbackMessage, Throwable cause) {
        super(exceptionCode, fallbackMessage, cause, PlatformErrorCode.INVALID_INPUT);
    }
}
