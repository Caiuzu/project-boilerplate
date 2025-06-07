package com.app.core.api.catalog.domain.exceptions;

import com.app.core.api.shared.exception.ResourceConflictException;

/**
 * Exceção lançada quando um conflito referente a entidade produto é encontrada
 */
public class ProductConflictException extends ResourceConflictException {

    public ProductConflictException(String message) {
        super(message);
    }
    public ProductConflictException(String resourceName, String fieldName, Object fieldValue) {
        super(resourceName, fieldName, fieldValue);
    }
}
