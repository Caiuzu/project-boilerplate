package com.app.core.api.payment.domain.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Evento de domínio emitido quando um pagamento é aprovado
 */
@Getter
@AllArgsConstructor
public class PaymentApprovedEvent {
    private final Long paymentId;
    private final Long orderId;
    private final BigDecimal amount;
    private final String paymentMethod;
    private final LocalDateTime approvedAt;
    
    /**
     * Cria um evento de pagamento aprovado
     * 
     * @param paymentId ID do pagamento
     * @param orderId ID do pedido
     * @param amount Valor do pagamento
     * @param paymentMethod Método de pagamento
     * @return Nova instância do evento
     */
    public static PaymentApprovedEvent of(Long paymentId, Long orderId, BigDecimal amount, String paymentMethod) {
        return new PaymentApprovedEvent(paymentId, orderId, amount, paymentMethod, LocalDateTime.now());
    }
} 