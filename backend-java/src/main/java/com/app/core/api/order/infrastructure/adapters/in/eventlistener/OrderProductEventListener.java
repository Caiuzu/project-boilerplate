package com.app.core.api.order.infrastructure.adapters.in.eventlistener;

import com.app.core.api.catalog.domain.events.ProductCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Ouvinte de eventos de catalogo no módulo de pedidos
 */
@Component
public class OrderProductEventListener {
    
    private static final Logger logger = LoggerFactory.getLogger(OrderProductEventListener.class);
    
    /**
     * Processa o evento de produto criado
     * 
     * @param event Evento de produto criado
     */
    @EventListener
    public void handleProductCreatedEvent(ProductCreatedEvent event) {
        logger.info("Módulo Order: Notificado da criação do produto: {} (ID: {}), com preço: {}",
                event.getProductName(), event.getProductId(), event.getPrice());
    }
} 