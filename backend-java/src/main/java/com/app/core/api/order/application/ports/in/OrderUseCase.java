package com.app.core.api.order.application.ports.in;

import com.app.core.api.order.application.dto.request.CreateOrderRequest;
import com.app.core.api.order.application.dto.request.OrderStatusRequest;
import com.app.core.api.order.application.dto.response.OrderResponse;
import com.app.core.api.order.application.dto.response.OrderStatusResponse;
import com.app.core.api.order.domain.vo.OrderStatus;

import java.util.List;

/**
 * Interface do caso de uso de pedidos
 */
public interface OrderUseCase {

    /**
     * Cria um novo pedido
     *
     * @param createOrderRequest dados de criação do pedido
     * @return Pedido criado
     */
    OrderResponse createOrder(CreateOrderRequest createOrderRequest);

    /**
     * Atualiza o status de um pedido
     *
     * @param orderId ID do pedido
     * @param orderStatus novo status do pedido
     */
    void updateOrderStatus(Long orderId, OrderStatus orderStatus);

    /**
     * Atualiza o status de um pedido
     *
     * @param orderId ID do pedido
     * @param orderStatusRequest novo status do pedido
     */
    OrderStatusResponse updateOrderStatus(Long orderId, OrderStatusRequest orderStatusRequest);

    /**
     * Busca pedidos que não estejam finalizados, ordenados por prioridade de status e data de criação.
     * A ordem de prioridade de status é: PRONTO > EM_PREPARACAO > RECEBIDO.
     * Pedidos com status FINALIZADO não são retornados.
     *
     */
    List<OrderResponse> getActiveOrdersSorted();
} 