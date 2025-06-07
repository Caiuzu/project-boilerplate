package com.app.core.api.shared.exception;

/**
 * Exceção lançada quando existe um erro de retorno de uma API
 */
public class APIException extends RuntimeException {

    private final int statusCode;

    public APIException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public APIException(String message, Throwable cause, int statusCode) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}