package com.app.core.api.order.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO para representar um item de pedido na requisição
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Item do pedido")
public class OrderItemRequest {

    @NotNull(message = "O ID do produto é obrigatório")
    @Positive(message = "O ID do produto deve ser positivo")
    @Schema(description = "ID do produto", example = "1", required = true)
    private Long productId;

    @NotEmpty(message = "O nome do produto do item do pedido é obrigatório")
    @Schema(description = "Nome do produto do item do pedido", example = "X-Burger", required = true)
    private String name = "";

    @NotNull(message = "A quantidade é obrigatória")
    @Positive(message = "A quantidade deve ser positiva")
    @Schema(description = "Quantidade do produto", example = "2", required = true)
    private Integer quantity;

    @NotNull(message = "O preço unitário é obrigatório")
    @DecimalMin(value = "0.01", message = "O preço unitário deve ser maior que zero")
    @Schema(description = "Preço unitário do produto", example = "22.90", required = true)
    private BigDecimal unitPrice;

    @Schema(description = "Observações adicionais sobre o item", example = "Sem cebola")
    private String observations = "";
}