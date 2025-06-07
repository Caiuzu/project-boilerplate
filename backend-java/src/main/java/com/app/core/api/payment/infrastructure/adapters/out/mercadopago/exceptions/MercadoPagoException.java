package com.app.core.api.payment.infrastructure.adapters.out.mercadopago.exceptions;

import com.app.core.api.shared.exception.APIException;

/**
 * Exceção lançada quando existe um erro de retorno da API do Mercado Pago
 */
public class MercadoPagoException extends APIException {

    public MercadoPagoException(String message, int statusCode) {
        super(message, statusCode);
    }

    public MercadoPagoException(String message, Throwable cause, int statusCode) {
        super(message, cause, statusCode);
    }
}