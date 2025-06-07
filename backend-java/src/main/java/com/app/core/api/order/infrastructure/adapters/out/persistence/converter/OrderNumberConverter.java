package com.app.core.api.order.infrastructure.adapters.out.persistence.converter;

import com.app.core.api.order.domain.vo.OrderNumber;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Conversor JPA para o Value Object {@link OrderNumber}.
 * <p>
 * Este conversor permite que o objeto {@code OrderNumber} seja persistido em uma única coluna
 * do banco de dados como uma {@code String} no formato: {@code "ORD-YYYY-NNNNN"}.
 * <p>
 * Também realiza a conversão reversa da {@code String} do banco para uma instância de {@code OrderNumber}.
 * <p>
 * Exemplo de valor persistido: {@code "ORD-2025-00042"}
 *
 * <p><strong>Importante:</strong> Este conversor deve ser usado em conjunto com a anotação {@code @Convert}.
 *
 * @see OrderNumber
 * @see jakarta.persistence.AttributeConverter
 */
@Converter(autoApply = false)
public class OrderNumberConverter implements AttributeConverter<OrderNumber, String> {

    /**
     * Converte o {@code OrderNumber} para uma {@code String} formatada.
     *
     * @param attribute o objeto {@code OrderNumber} a ser convertido
     * @return uma {@code String} no formato {@code "ORD-YYYY-NNNNN"}, ou {@code null} se o atributo for nulo
     */
    @Override
    public String convertToDatabaseColumn(OrderNumber attribute) {
        if (attribute == null) return null;
        return attribute.getFormatted();
    }

    /**
     * Converte a {@code String} do banco de dados de volta para um objeto {@code OrderNumber}.
     *
     * @param dbData a {@code String} recuperada do banco no formato {@code "ORD-YYYY-NNNNN"}
     * @return uma instância de {@code OrderNumber}, ou {@code null} se a string for nula
     * @throws IllegalArgumentException se o formato estiver inválido
     */
    @Override
    public OrderNumber convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;

        // Espera o formato "ORD-YYYY-NNNNN"
        String[] parts = dbData.split("-");

        if (parts.length != 3 || !parts[0].equals("ORD")) {
            throw new IllegalArgumentException("Formato inválido para OrderNumber: " + dbData);
        }

        int year = Integer.parseInt(parts[1]);
        int sequential = Integer.parseInt(parts[2]);

        return new OrderNumber(year, sequential);
    }
}

