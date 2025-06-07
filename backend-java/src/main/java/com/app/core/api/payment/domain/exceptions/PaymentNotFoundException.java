package com.app.core.api.payment.domain.exceptions;

import com.app.core.api.shared.exception.ResourceNotFoundException;

/**
 * Exceção lançada quando um pagamento não é encontrado
 */
public class PaymentNotFoundException extends ResourceNotFoundException {

    public PaymentNotFoundException(String message) {
        super(message);
    }
    public PaymentNotFoundException(String message, Long id) {
        super(message, id);
    }
}
