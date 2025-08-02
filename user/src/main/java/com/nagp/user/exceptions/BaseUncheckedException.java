package com.nagp.user.exceptions;
public abstract class BaseUncheckedException extends RuntimeException implements BaseException {
    protected ErrorCode errorCode;
    protected String exceptionCode;
    protected String fallbackMessage;

    public BaseUncheckedException(String exceptionCode, ErrorCode errorCode) {
        super(exceptionCode);
        this.exceptionCode = exceptionCode;
        this.errorCode = errorCode;
    }

    public BaseUncheckedException(String exceptionCode, Throwable cause, ErrorCode errorCode) {
        super(exceptionCode, cause);
        this.exceptionCode = exceptionCode;
        this.errorCode = errorCode;
    }

    public BaseUncheckedException(String exceptionCode, String fallbackMessage, ErrorCode errorCode) {
        super(exceptionCode);
        this.exceptionCode = exceptionCode;
        this.errorCode = errorCode;
        this.fallbackMessage = fallbackMessage;
    }

    public BaseUncheckedException(String exceptionCode, String fallbackMessage, Throwable cause, ErrorCode errorCode) {
        super(exceptionCode, cause);
        this.exceptionCode = exceptionCode;
        this.errorCode = errorCode;
        this.fallbackMessage = fallbackMessage;
    }

    public String exceptionCode() {
        return this.exceptionCode;
    }

    public ErrorCode errorCode() {
        return this.errorCode;
    }

    public String fallbackMessage() {
        return this.fallbackMessage;
    }
}

