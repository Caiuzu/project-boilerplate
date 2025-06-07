package com.app.core.api.payment.infrastructure.adapters.in.eventlistener;

import com.app.core.api.order.domain.events.OrderCreatedEvent;
import com.app.core.api.payment.application.ports.in.PaymentUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Ouvinte de eventos de pedido no módulo de pagamento
 */
@Component
@Slf4j
public class PaymentOrderEventListener {
    
    private final PaymentUseCase paymentUseCase;
    
    public PaymentOrderEventListener(PaymentUseCase paymentUseCase) {
        this.paymentUseCase = paymentUseCase;
    }
    
    /**
     * Processa o evento de pedido criado iniciando um processo de pagamento
     * 
     * @param event Evento de pedido criado
     */
    @EventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOrderCreatedEvent(OrderCreatedEvent event) {
        log.info("Módulo Payment: Iniciando pagamento para o pedido: {} com valor total: {}",
                event.getId(), event.getTotalAmount());
        
        paymentUseCase.initializePayment(event);

    }
} 