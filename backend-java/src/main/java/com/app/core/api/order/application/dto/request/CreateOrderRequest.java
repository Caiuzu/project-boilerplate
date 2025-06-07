package com.app.core.api.order.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO para criação de um novo pedido
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Requisição para criação de um novo pedido")
public class CreateOrderRequest {

    @Schema(description = "ID do cliente (opcional)", example = "1")
    private Long userId;

    @NotEmpty(message = "A lista de itens não pode estar vazia")
    @Valid
    @Schema(description = "Lista de itens do pedido", required = true)
    private List<OrderItemRequest> items;
} 