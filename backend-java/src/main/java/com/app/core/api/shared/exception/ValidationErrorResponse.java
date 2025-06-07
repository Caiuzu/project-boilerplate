package com.app.core.api.shared.exception;

import java.time.LocalDateTime;
import java.util.Map;
import lombok.Getter;

/**
 * Resposta de erro de validação
 */
@Getter
public class ValidationErrorResponse extends ErrorResponse {
    private final Map<String, String> errors;

    public ValidationErrorResponse(LocalDateTime timestamp, int status, String message, String path, Map<String, String> errors) {
        super(timestamp, status, message, path);
        this.errors = errors;
    }
} 