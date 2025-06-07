package com.app.core.api.shared.exception;

/**
 * Exceção para recursos não encontrados
 */
public class ResourceNotFoundException extends RuntimeException {
    
    public ResourceNotFoundException(String message) {
        super(message);
    }
    
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s não encontrado com %s: %s", resourceName, fieldName, fieldValue));
    }
    
    public ResourceNotFoundException(String resourceName, Long id) {
        super(String.format("%s não encontrado com id: %d", resourceName, id));
    }
} 