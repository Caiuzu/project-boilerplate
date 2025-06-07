package com.app.core.api.payment.application.dto.response;

/**
 * DTO para o status de um pedido no adquirente
 */
public enum AcquirerOrderStatus {
    PAID("Paga"),
    PAYMENT_REQUIRED("Pagamento requerido");

    private final String description;

    AcquirerOrderStatus(String description) {
        this.description = description;
    }

    public static AcquirerOrderStatus fromValue(String value) {

        String normalized = value.trim().toUpperCase().replace("-", "_").replace(" ", "_");

        for (AcquirerOrderStatus method : values()) {
            if (method.name().equalsIgnoreCase(normalized)) {
                return method;
            }
        }

        throw new IllegalArgumentException("AcquirerOrderStatus desconhecido: " + value);
    }
}
