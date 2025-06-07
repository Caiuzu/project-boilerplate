package com.app.core.api.catalog.domain.exceptions;

import com.app.core.api.shared.exception.BusinessException;

/**
 * Exceção lançada quando uma regra de negócio referente a entidade categoria é violada
 */
public class CategoryException extends BusinessException {

    public CategoryException(String message) {
        super(message);
    }

    public CategoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
