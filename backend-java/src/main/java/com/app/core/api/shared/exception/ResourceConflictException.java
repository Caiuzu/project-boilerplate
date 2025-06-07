package com.app.core.api.shared.exception;

/**
 * Exceção para conflitos de recursos (ex: CPF duplicado)
 */
public class ResourceConflictException extends RuntimeException {
    
    public ResourceConflictException(String message) {
        super(message);
    }
    
    public ResourceConflictException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("Já existe um(a) %s com %s: %s", resourceName, fieldName, fieldValue));
    }
} 