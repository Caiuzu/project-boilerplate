package com.app.core.api.order.application.dto.request;

import com.app.core.api.order.domain.vo.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para requisição de atualização do status de um pedido
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusRequest {
    @Schema(description = "Status do pedido")
    private OrderStatus orderStatus;
} 