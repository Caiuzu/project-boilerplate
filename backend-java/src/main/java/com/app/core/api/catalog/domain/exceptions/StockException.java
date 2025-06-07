package com.app.core.api.catalog.domain.exceptions;

import com.app.core.api.shared.exception.BusinessException;

/**
 * Exceção lançada quando uma regra de negócio referente a entidade estoque é violada
 */
public class StockException extends BusinessException {

    public StockException(String message) {
        super(message);
    }

    public StockException(String message, Throwable cause) {
        super(message, cause);
    }
}
