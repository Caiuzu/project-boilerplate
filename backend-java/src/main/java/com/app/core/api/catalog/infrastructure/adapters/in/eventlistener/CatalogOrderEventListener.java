package com.app.core.api.catalog.infrastructure.adapters.in.eventlistener;

import com.app.core.api.catalog.application.ports.in.CatalogUseCase;
import com.app.core.api.order.domain.events.OrderCanceledEvent;
import com.app.core.api.order.domain.events.OrderCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Ouvinte de eventos de pedido no módulo de catalogo
 */
@Component
@Slf4j
public class CatalogOrderEventListener {

    private final CatalogUseCase catalogUseCase;

    public CatalogOrderEventListener(CatalogUseCase catalogUseCase) {
        this.catalogUseCase = catalogUseCase;
    }
    
    /**
     * Processa o evento de pedido criado
     * 
     * @param event Evento de pedido criado
     */
    @EventListener
    public void handleOrderCreatedEvent(OrderCreatedEvent event) {
        log.info("Módulo Catalogo: Notificado de criação de pedido: {}),",
                event.getId());

        catalogUseCase.updateStockForCreatedItems(event.getItems());

        log.info("Quantidade em estoque atualizada para: {} produtos.", event.getItems().size());
    }

    /**
     * Processa o evento de pedido cancelado
     *
     * @param event Evento de pedido cancelado
     */
    @EventListener
    public void handleOrderCanceledEvent(OrderCanceledEvent event) {
        log.info("Módulo Catalogo: Notificado de cancelamento de pedido: {}),",
                event.getId());

        catalogUseCase.updateStockForCanceledItems(event.getItems());

        log.info("Quantidade em estoque atualizada para: {} produtos.", event.getItems().size());
    }
} 