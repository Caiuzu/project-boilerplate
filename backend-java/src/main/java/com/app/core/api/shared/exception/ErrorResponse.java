package com.app.core.api.shared.exception;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.AllArgsConstructor;

/**
 * Resposta de erro padrão
 */
@Getter
@AllArgsConstructor
public class ErrorResponse {
    private final LocalDateTime timestamp;
    private final int status;
    private final String message;
    private final String path;
} 