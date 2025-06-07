package com.app.core.api.order.domain.events;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Evento de domínio emitido quando um pedido é criado
 */
@Data
public class OrderItemCreatedEvent {

    private Long id;

    private Long productId;

    private String name;

    private Integer quantity;

    private BigDecimal unitPrice;

    private BigDecimal subtotal;

    private String observations;
}
