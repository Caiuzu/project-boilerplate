package com.app.core.api.catalog.domain.exceptions;

import com.app.core.api.shared.exception.ResourceNotFoundException;

/**
 * Exceção lançada quando uma categoria não é encontrada
 */
public class CategoryNotFoundException extends ResourceNotFoundException {

    public CategoryNotFoundException(String message) {
        super(message);
    }
    public CategoryNotFoundException(String message, Long id) {
        super(message, id);
    }
}
