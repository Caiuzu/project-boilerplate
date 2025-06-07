package com.app.core.api.payment.infrastructure.adapters.in.controller;

import com.app.core.api.payment.application.dto.request.AcquirerNotificationRequest;
import com.app.core.api.payment.application.dto.request.AcquirerTopicNotificationRequest;
import com.app.core.api.payment.application.dto.response.AcquirerOrderResponse;
import com.app.core.api.payment.application.dto.response.PaymentStatusResponse;
import com.app.core.api.payment.application.dto.response.QrCodeResponse;
import com.app.core.api.payment.application.ports.in.PaymentUseCase;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para pagamentos
 */
@RestController
@RequestMapping("/payments")
@Slf4j
public class PaymentController {

    private final PaymentUseCase paymentUseCase;

    public PaymentController(PaymentUseCase paymentUseCase) {
        this.paymentUseCase = paymentUseCase;
    }


    // ========== ADQUIRENTE ==========

    @Hidden
    @Operation(
            operationId = "webhookWithTopic",
            summary = "Webhook com parâmetros 'topic' e 'id'",
            description = "Recebe notificações simplificadas do adquirente com parâmetros 'topic' e 'id' na URL",
            parameters = {
                    @Parameter(name = "topic",
                            description = "Tipo do tópico da notificação (ex: 'merchant_order')",
                            required = true,
                    in = ParameterIn.QUERY),
                    @Parameter(name = "id",
                            description = "ID do recurso associado à notificação (ex: ID da merchant_order)",
                            required = true,
                            in = ParameterIn.QUERY)
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notificação processada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Notificação malformada")
    })
    @PostMapping(value = "/webhook", params = {"topic", "id"})
    @Tag(name = "Mercado Pago", description = "Endpoints de integração com o adquirente")
    public ResponseEntity<Void> acquirerTopicWebhook(
            @RequestParam String topic,
            @RequestParam String id,
            @Valid @RequestBody AcquirerTopicNotificationRequest notification
    ) {
        log.info("Recebida notificação do adquirente (topic): topic={}, resource={}", topic, notification.getResource());

        return ResponseEntity.ok().build();
    }

    @Operation(
            operationId = "webhookWithoutTopic",
            summary = "Webhook completo do adquirente",
            description = "Recebe notificações de eventos de pagamento do adquirente com corpo JSON completo"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notificação processada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Notificação malformada")
    })
    @PostMapping(value = "/webhook", params = "!topic")
    @Tag(name = "Mercado Pago", description = "Endpoints de integração com o adquirente")
    public ResponseEntity<Void> acquirerWebhook(@Valid @RequestBody AcquirerNotificationRequest notification) {
        log.info("Recebida notificação do adquirente (completa): ação={}, id interno={}, id externo={}",
                notification.getAction(),
                notification.getId(),
                notification.getData() != null ? notification.getData().getId() : "sem id externo");

        paymentUseCase.processPaymentNotification(notification);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Buscar pedido no adquirente por ID da merchant_order", security = @SecurityRequirement(name = "bearer-key"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido retornado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AcquirerOrderResponse.class))),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado", content = @Content)
    })
    @GetMapping("/merchant_orders/{merchantOrderId}")
    @Tag(name = "Mercado Pago", description = "Endpoints de integração com o adquirente")
    public ResponseEntity<AcquirerOrderResponse> getAcquirerOrder(@PathVariable Long merchantOrderId) {
        log.info("Recebida requisição para obter dados do pedido no adquirente. merchantOrderId={}", merchantOrderId);
        var response = paymentUseCase.getAcquirerOrder(merchantOrderId);
        return ResponseEntity.ok(response);
    }

    // ========== PAGAMENTOS ==========

    @Operation(summary = "Buscar status do pagamento por ID do pedido", security = @SecurityRequirement(name = "bearer-key"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status do pagamento retornado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentStatusResponse.class))),
            @ApiResponse(responseCode = "404", description = "Pagamento não encontrado", content = @Content)
    })
    @GetMapping("/{orderId}/status")
    @Tag(name = "Pagamentos", description = "Operações para gerenciamento de pagamentos")
    public ResponseEntity<PaymentStatusResponse> getOrderPaymentStatus(@PathVariable Long orderId) {
        log.info("Recebida requisição para obter status do pagamento para orderId {}", orderId);
        var response = paymentUseCase.getOrderPaymentStatus(orderId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Buscar QrCode de pagamento por ID do pedido", security = @SecurityRequirement(name = "bearer-key"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Qr Code de pagamento retornado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = QrCodeResponse.class))),
            @ApiResponse(responseCode = "404", description = "Pagamento não encontrado", content = @Content)
    })
    @GetMapping("/{orderId}/qrCode")
    @Tag(name = "Pagamentos", description = "Operações para gerenciamento de pagamentos")
    public ResponseEntity<QrCodeResponse> getOrderPaymentQrCode(@PathVariable Long orderId) {
        log.info("Recebida requisição para obter qr de pagamento para orderId {}", orderId);
        var response = paymentUseCase.getOrderPaymentQrCode(orderId);
        return ResponseEntity.ok(response);
    }
}