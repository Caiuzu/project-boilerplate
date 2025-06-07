package com.app.core.api.payment.domain.vo;

import lombok.Getter;

/**
 * Enum que representa os possíveis status de um pagamento
 */
@Getter
public enum PaymentStatus {
    PENDING("Pendente"),
    APPROVED("Aprovado"),
    REJECTED("Rejeitado"),
    CANCELLED("Cancelado");
    
    private final String description;
    
    PaymentStatus(String description) {
        this.description = description;
    }

    public static PaymentStatus fromValue(String value) {
        for (PaymentStatus status : values()) {
            if (status.name().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Status desconhecido: " + value);
    }

} 