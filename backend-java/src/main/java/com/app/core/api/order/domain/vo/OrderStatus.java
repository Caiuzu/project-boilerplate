package com.app.core.api.order.domain.vo;

import lombok.Getter;

/**
 * Enum que representa os possíveis status de um pedido
 */
@Getter
public enum OrderStatus {
    RECEIVED("Recebido"),
    PREPARING("Em Preparação"),
    READY("Pronto"),
    COMPLETED("Finalizado"),
    CANCELLED("Cancelado");
    
    private final String description;
    
    OrderStatus(String description) {
        this.description = description;
    }

}
