package com.app.core.api.payment.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Entidade que representa o body de envio de notificações simplificadas do adquirente
 */
@Data
@Schema(description = "Notificação simplificada recebida do adquirente via Webhook (topic e id)")
public class AcquirerTopicNotificationRequest {

    @NotBlank(message = "O campo 'topic' não pode estar em branco")
    @Schema(description = "Tipo de recurso notificado (ex: merchant_order ou payment)", example = "merchant_order")
    private String topic;

    @NotNull(message = "O campo 'resource' não pode ser nulo")
    @Schema(description = "URL de recurso relacionada à notificação", example = "https://api.mercadolibre.com/merchant_orders/123123")
    private String resource;
}


