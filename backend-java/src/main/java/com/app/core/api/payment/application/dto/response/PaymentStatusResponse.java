package com.app.core.api.payment.application.dto.response;

import com.app.core.api.payment.domain.vo.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para resposta de checagem de pagamento
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentStatusResponse {
    @Schema(description = "ID do pedido")
    private Long orderId;
    @Schema(description = "Status de pagamento do pedido")
    private PaymentStatus status;
} 