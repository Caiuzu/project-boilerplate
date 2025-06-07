package com.app.core.api.catalog.domain.exceptions;

import com.app.core.api.shared.exception.BusinessException;

/**
 * Exceção lançada quando uma regra de negócio referente a entidade produto é violada
 */
public class ProductException extends BusinessException {

    public ProductException(String message) {
        super(message);
    }

    public ProductException(String message, Throwable cause) {
        super(message, cause);
    }
}
