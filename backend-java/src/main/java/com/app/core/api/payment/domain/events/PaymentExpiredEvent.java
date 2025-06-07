package com.app.core.api.payment.domain.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Evento de domínio emitido quando um pagamento expirou
 */
@Getter
@AllArgsConstructor
public class PaymentExpiredEvent {
    private final Long paymentId;
    private final Long orderId;
    private final LocalDateTime expiredIn;
} 