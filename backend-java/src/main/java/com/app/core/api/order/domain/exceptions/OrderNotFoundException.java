package com.app.core.api.order.domain.exceptions;

import com.app.core.api.shared.exception.ResourceNotFoundException;

/**
 * Exceção lançada quando um pedido não é encontrado
 */
public class OrderNotFoundException extends ResourceNotFoundException {

    public OrderNotFoundException(String message) {
        super(message);
    }
    public OrderNotFoundException(String message, Long id) {
        super(message, id);
    }
}
