package com.app.core.api.payment.application.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.ZonedDateTime;

/**
 * Entidade que representa o body de envio de notificações por parte do adquirente
 *
 */
@Data
@Schema(description = "Notificação recebida do adquirente via Webhook")
public class AcquirerNotificationRequest {

    @NotNull(message = "O campo 'id' não pode ser nulo")
    @Schema(description = "Identificador da notificação", example = "123456789")
    private Long id;

    @Schema(description = "Indica se a notificação foi enviada em modo live", example = "false")
    @JsonProperty("live_mode")
    private boolean liveMode;

    @NotBlank(message = "O campo 'type' não pode estar em branco")
    @Schema(description = "Tipo da notificação", example = "payment")
    private String type;

    @NotNull(message = "O campo 'date_created' não pode ser nulo")
    @Schema(description = "Data de criação da notificação", example = "2023-05-01T12:34:56Z")
    @JsonProperty("date_created")
    private ZonedDateTime dateCreated;

    @NotNull(message = "O campo 'user_id' não pode ser nulo")
    @Schema(description = "Identificador do usuário remetente da notificação", example = "987654321")
    @JsonProperty("user_id")
    private Long userId;

    @NotBlank(message = "O campo 'api_version' não pode estar em branco")
    @Schema(description = "Versão da API utilizada", example = "v1")
    @JsonProperty("api_version")
    private String apiVersion;

    @NotBlank(message = "O campo 'action' não pode estar em branco")
    @Schema(description = "Ação da notificação", example = "payment.created")
    private String action;

    @NotNull(message = "O campo 'data' não pode ser nulo")
    @Valid
    @Schema(description = "Objeto contendo dados adicionais da notificação")
    private Data data;

    @lombok.Data
    @Schema(description = "Objeto com o ID da entidade relacionada à notificação")
    public static class Data {

        @NotBlank(message = "O campo 'id' de data não pode estar em branco")
        @Schema(description = "Identificador da entidade associada (ex: pagamento)", example = "1234567890")
        private String id;
    }

    public String getDataId() {
        return this.data.getId();
    }
}

