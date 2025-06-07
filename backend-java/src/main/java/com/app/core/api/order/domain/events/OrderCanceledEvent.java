package com.app.core.api.order.domain.events;

import com.app.core.api.order.domain.vo.OrderStatus;
import lombok.Data;

import java.util.List;

/**
 * Evento de domínio emitido quando um pedido é cancelado
 */
@Data
public class OrderCanceledEvent {

    private Long id;

    private OrderStatus status;

    private List<OrderItemCanceledEvent> items;

} 