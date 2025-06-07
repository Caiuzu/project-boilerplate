package com.app.core.api.catalog.domain.exceptions;

import com.app.core.api.shared.exception.ResourceConflictException;

/**
 * Exceção lançada quando um conflito referente a entidade catalogo é encontrada
 */
public class CatalogConflictException extends ResourceConflictException {

    public CatalogConflictException(String message) {
        super(message);
    }
    public CatalogConflictException(String resourceName, String fieldName, Object fieldValue) {
        super(resourceName, fieldName, fieldValue);
    }
}
