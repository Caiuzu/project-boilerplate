package com.app.core.api.order.infrastructure.adapters.in.controller;

import com.app.core.api.order.application.dto.request.CreateOrderRequest;
import com.app.core.api.order.application.dto.request.OrderStatusRequest;
import com.app.core.api.order.application.dto.response.OrderResponse;
import com.app.core.api.order.application.dto.response.OrderStatusResponse;
import com.app.core.api.order.application.ports.in.OrderUseCase;
import com.app.core.api.shared.infrastructure.adapters.out.logging.CustomLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gerenciamento de pedidos
 */
@RestController
@RequestMapping("/orders")
@Tag(name = "Pedidos", description = "API para gerenciamento de pedidos")
public class OrderController {

    private final OrderUseCase orderUseCase;
    private final CustomLogger logger;

    public OrderController(
            OrderUseCase orderUseCase) {
        this.orderUseCase = orderUseCase;
        this.logger = CustomLogger.getLogger(getClass());
    }

    @PostMapping
    @Operation(
            summary = "Criar novo pedido",
            description = "Cria um novo pedido com os itens, produtos e descontos especificados",
            security = @SecurityRequirement(name = "bearer-key")
    )

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Pedido criado com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = OrderResponse.class))

            ),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Cliente ou produto não encontrado", content = @Content)
    })
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest createOrderRequest) {
        logger.debug("Requisição para criar novo pedido recebida");
        OrderResponse orderResponse = orderUseCase.createOrder(createOrderRequest);
        return ResponseEntity.status(201).body(orderResponse);
    }

    @GetMapping("/active")
    @Operation(
            summary = "Listar pedidos ativos ordenados",
            description = "Retorna todos os pedidos com status RECEBIDO, EM_PREPARACAO ou PRONTO, ordenados por prioridade e data de criação",
            security = @SecurityRequirement(name = "bearer-key")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de pedidos ativos retornada com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = OrderResponse.class))
            )
    })
    public ResponseEntity<List<OrderResponse>> getActiveOrders() {
        logger.debug("Requisição para listar pedidos ativos recebida");

        List<OrderResponse> activeOrders = orderUseCase.getActiveOrdersSorted();

        return ResponseEntity.ok(activeOrders);
    }

    @PatchMapping("/{orderId}/status")
    @Operation(
            summary = "Atualizar status do pedido",
            description = "Atualiza o status de um pedido existente para um dos valores válidos: RECEIVED, PREPARING, READY, COMPLETED",
            security = @SecurityRequirement(name = "bearer-key")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Status do pedido atualizado com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = OrderStatusResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Status inválido ou dados malformados", content = @Content),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado", content = @Content)
    })
    public ResponseEntity<OrderStatusResponse> updateOrderStatus(
            @PathVariable Long orderId,
            @Valid @RequestBody OrderStatusRequest orderStatusRequest) {

        logger.debug("Requisição para atualizar status do pedido {} recebida", orderId);

        OrderStatusResponse response = orderUseCase.updateOrderStatus(orderId, orderStatusRequest);

        return ResponseEntity.ok(response);
    }
} 