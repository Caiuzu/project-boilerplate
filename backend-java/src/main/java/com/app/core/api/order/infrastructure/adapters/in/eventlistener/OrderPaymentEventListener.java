package com.app.core.api.order.infrastructure.adapters.in.eventlistener;

import com.app.core.api.order.application.ports.in.OrderUseCase;
import com.app.core.api.order.domain.vo.OrderStatus;
import com.app.core.api.payment.domain.events.PaymentApprovedEvent;
import com.app.core.api.payment.domain.events.PaymentExpiredEvent;
import com.app.core.api.payment.domain.events.PaymentInitializationErrorEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Ouvinte de eventos de pagamento no módulo de pedidos
 */
@Component
@Slf4j
public class OrderPaymentEventListener {
    
    private final OrderUseCase orderUseCase;
    
    public OrderPaymentEventListener(OrderUseCase orderUseCase) {
        this.orderUseCase = orderUseCase;
    }
    
    /**
     * Processa o evento de pagamento aprovado
     * 
     * @param event Evento de pagamento aprovado
     */
    @EventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handlePaymentApprovedEvent(PaymentApprovedEvent event) {
        log.info("Módulo Order: Recebido evento de pagamento aprovado para o pedido: {}, valor: {}",
                event.getOrderId(), event.getAmount());
        
          orderUseCase.updateOrderStatus(event.getOrderId(), OrderStatus.PREPARING);

          log.info("Pedido {} atualizado para status PREPARING após pagamento aprovado", event.getOrderId());
    }

    /**
     * Processa o evento de erro na inicialização do pagamento
     *
     * @param event Evento de erro na inicialização do pagamento
     */
    @EventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handlePaymentInitializationErrorEvent(PaymentInitializationErrorEvent event) {
        log.info("Módulo Order: Recebido evento de erro na inicialização do pagamento. Pedido: {}", event.getOrderId());

        orderUseCase.updateOrderStatus(event.getOrderId(), OrderStatus.CANCELLED);
    }

    /**
     * Processa eventos de pagamento expirado
     *
     * @param event Evento de pagamento expirado
     */
    @EventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handlePaymentExpiredEvent(PaymentExpiredEvent event) {
        log.info("Módulo Order: Recebido evento de pagamento expirado. Pedido: {}, Pagamento: {}", event.getOrderId(), event.getPaymentId());

        orderUseCase.updateOrderStatus(event.getOrderId(), OrderStatus.CANCELLED);
    }
} 