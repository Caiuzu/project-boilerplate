package com.app.core.api.catalog.domain.exceptions;

import com.app.core.api.shared.exception.ResourceNotFoundException;

/**
 * Exceção lançada quando um catalogo não é encontrado
 */
public class CatalogNotFoundException extends ResourceNotFoundException {

    public CatalogNotFoundException(String message) {
        super(message);
    }
    public CatalogNotFoundException(String message, Long id) {
        super(message, id);
    }
}
