package com.app.core.api.order.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para resposta de item de pedido
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Item de um pedido")
public class OrderItemResponse {

    @Schema(description = "ID do item", example = "1")
    private Long id;

    @Schema(description = "ID do produto", example = "3")
    private Long productId;

    @Schema(description = "Nome do produto do item do pedido", example = "Água Mineral")
    private String name;

    @Schema(description = "Quantidade", example = "2")
    private Integer quantity;

    @Schema(description = "Preço unitário", example = "25.90")
    private BigDecimal unitPrice;

    @Schema(description = "Subtotal do item", example = "51.80")
    private BigDecimal subtotal;

    @Schema(description = "Observações do item", example = "Sem tomate")
    private String observations;

    @Schema(description = "Data de criação", example = "2023-06-15T14:30:15")
    private LocalDateTime createdAt;

    @Schema(description = "Data de atualização", example = "2023-06-15T14:45:22")
    private LocalDateTime updatedAt;
}