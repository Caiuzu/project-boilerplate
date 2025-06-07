package com.app.core.api.payment.domain.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Evento de domínio emitido quando ocorre um erro na inicialização do pagamento
 */
@Getter
@AllArgsConstructor
public class PaymentInitializationErrorEvent {
    private final Long orderId;
    private final String errorMessage;
}