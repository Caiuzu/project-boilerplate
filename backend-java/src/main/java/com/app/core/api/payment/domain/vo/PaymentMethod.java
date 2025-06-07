package com.app.core.api.payment.domain.vo;

import lombok.Getter;

/**
 * Enum que representa os métodos de pagamento disponíveis
 */
@Getter
public enum PaymentMethod {
    CREDIT_CARD("Cartão de Crédito"),
    DEBIT_CARD("Cartão de Débito"),
    PIX("PIX"),
    ACCOUNT_MONEY("Dinheiro");
    
    private final String description;
    
    PaymentMethod(String description) {
        this.description = description;
    }

    public static PaymentMethod fromValue(String value) {

        String normalized = value.trim().toUpperCase().replace("-", "_").replace(" ", "_");

        for (PaymentMethod method : values()) {
            if (method.name().equalsIgnoreCase(normalized)) {
                return method;
            }
        }

        throw new IllegalArgumentException("PaymentMethod desconhecido: " + value);
    }

} 