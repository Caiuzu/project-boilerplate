package com.app.core.api.order.application.dto.request;

import com.app.core.api.order.domain.vo.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para atualização de status de um pedido
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Requisição para atualização de status de um pedido")
public class UpdateOrderStatusRequest {

    @NotNull(message = "O status é obrigatório")
    @Schema(description = "Novo status do pedido", example = "PREPARING", required = true)
    private OrderStatus status;
} 