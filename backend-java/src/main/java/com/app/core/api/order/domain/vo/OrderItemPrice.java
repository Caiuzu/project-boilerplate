package com.app.core.api.order.domain.vo;

import org.apache.commons.lang3.Validate;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Value Object que representa o preço do item de um pedido
 */
public record OrderItemPrice(int quantity, BigDecimal unitPrice) implements Serializable {

    /**
     * Cria um novo preço para o item.
     *
     * @param quantity      unidades do item
     * @param unitPrice     preço unitário do item
     */
    public OrderItemPrice {
        validate(quantity, unitPrice);
    }

    /**
     * Retorna a multiplicação entre a quantidade e o preço unitário do item
     * @return o subtotal do item do pedido
     */
    public BigDecimal getSubTotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    /**
     * Validação centralizada.
     *
     * @param quantity   Quantidade do item
     * @param unitPrice  Preço unitário do item
     * @throws IllegalArgumentException se a quantidade for menor ou igual a zero
     * @throws IllegalArgumentException se o preço unitário for menor ou igual a zero
     */
    private void validate(int quantity, BigDecimal unitPrice) {
        Validate.isTrue(quantity > 0, "A quantidade do item deve ser maior que 0");
        Validate.isTrue(unitPrice.compareTo(BigDecimal.ZERO) > 0, "O preço unitário do item deve ser maior que 0");
    }

}