package com.app.core.api.order.domain.vo;

import org.apache.commons.lang3.Validate;
import java.io.Serializable;

/**
 * Value Object que representa o número de um pedido.
 * Formato: "ORD-{ANO}-{SEQUENCIAL}" onde:
 * - ANO: 4 dígitos
 * - SEQUENCIAL: 5 dígitos
 * <p>
 * Exemplo: "ORD-2023-00042"
 */
public record OrderNumber(int year, long sequential) implements Serializable {

    /**
     * Cria um novo número de pedido.
     *
     * @param year       ano com 4 dígitos
     * @param sequential número sequencial com até 5 dígitos
     */
    public OrderNumber {
        validate(year);
    }

    /**
     * Retorna a representação formatada: "ORD-AAAA-NNNNN"
     *
     * @return String formatada do número do pedido
     */
    public String getFormatted() {
        return String.format("ORD-%04d-%05d", year, sequential);
    }

    /**
     * Validação centralizada.
     *
     * @param year        Ano com 4 dígitos
     * @throws IllegalArgumentException se o ano não tiver 4 dígitos
     * @throws IllegalArgumentException se o número sequencial for menor que 0 ou maior que 99999
     */
    private void validate(int year) {
        Validate.isTrue(year >= 1000 && year <= 9999, "Ano deve ter 4 dígitos");
    }
}