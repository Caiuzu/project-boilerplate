package com.app.core.api.catalog.domain.exceptions;

import com.app.core.api.shared.exception.ResourceConflictException;

/**
 * Exceção lançada quando um conflito referente a entidade categoria é encontrada
 */
public class CategoryConflictException extends ResourceConflictException {

    public CategoryConflictException(String message) {
        super(message);
    }
    public CategoryConflictException(String resourceName, String fieldName, Object fieldValue) {
        super(resourceName, fieldName, fieldValue);
    }
}
