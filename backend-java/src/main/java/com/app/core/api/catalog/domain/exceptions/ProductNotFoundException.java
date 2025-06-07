package com.app.core.api.catalog.domain.exceptions;

import com.app.core.api.shared.exception.ResourceNotFoundException;

/**
 * Exceção lançada quando um produto não é encontrado
 */
public class ProductNotFoundException extends ResourceNotFoundException {

    public ProductNotFoundException(String message) {
        super(message);
    }
    public ProductNotFoundException(String message, Long id) {
        super(message, id);
    }
}
