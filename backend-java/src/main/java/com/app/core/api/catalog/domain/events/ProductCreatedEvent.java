package com.app.core.api.catalog.domain.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Evento de domínio emitido quando um produto é criado
 */
@Getter
@AllArgsConstructor
public class ProductCreatedEvent {
    private final Long productId;
    private final String productName;
    private final BigDecimal price;
    private final Long categoryId;
    private final LocalDateTime timestamp;
    
    /**
     * Cria um evento de produto criado
     * 
     * @param productId ID do produto
     * @param productName Nome do produto
     * @param price Preço do produto
     * @param categoryId ID da categoria
     * @return Nova instância do evento
     */
    public static ProductCreatedEvent of(Long productId, String productName, BigDecimal price, Long categoryId) {
        return new ProductCreatedEvent(productId, productName, price, categoryId, LocalDateTime.now());
    }
} 