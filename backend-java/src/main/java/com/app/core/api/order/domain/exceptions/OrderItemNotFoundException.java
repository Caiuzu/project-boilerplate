package com.app.core.api.order.domain.exceptions;

import com.app.core.api.shared.exception.ResourceNotFoundException;

/**
 * Exceção lançada quando um item de pedido não é encontrado
 */
public class OrderItemNotFoundException extends ResourceNotFoundException {

    public OrderItemNotFoundException(String message) {
        super(message);
    }
    public OrderItemNotFoundException(String message, Long id) {
        super(message, id);
    }
}
