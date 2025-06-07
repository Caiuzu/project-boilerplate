package com.app.core.api.order.domain.exceptions;

import com.app.core.api.shared.exception.BusinessException;

/**
 * Exceção lançada quando uma regra de negócio é violada na entidade pedido
 */
public class OrderException extends BusinessException {

    public OrderException(String message) {
        super(message);
    }

    public OrderException(String message, Throwable cause) {
        super(message, cause);
    }
}
