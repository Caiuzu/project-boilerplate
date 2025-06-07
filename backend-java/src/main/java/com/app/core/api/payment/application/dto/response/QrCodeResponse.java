package com.app.core.api.payment.application.dto.response;

import com.app.core.api.payment.domain.vo.QrCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para resposta de obtenção do qrcode de um pedido
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QrCodeResponse {
    @Schema(description = "ID do pedido")
    private String orderId;
    @Schema(description = "Data de expiração do pagamento")
    private LocalDateTime expiresIn;
    @Schema(description = "Código QR do pedido")
    private QrCode qrCode;
} 