package com.app.core.api.order.application.services;


import com.app.core.api.order.application.dto.request.CreateOrderRequest;
import com.app.core.api.order.application.dto.request.OrderStatusRequest;
import com.app.core.api.order.application.dto.response.OrderResponse;
import com.app.core.api.order.application.dto.response.OrderStatusResponse;
import com.app.core.api.order.application.mapper.request.CreateOrderRequestMapper;
import com.app.core.api.order.application.mapper.response.OrderResponseMapper;
import com.app.core.api.order.application.mapper.response.OrderStatusResponseMapper;
import com.app.core.api.order.application.ports.in.OrderUseCase;
import com.app.core.api.order.domain.events.OrderCanceledEvent;
import com.app.core.api.order.domain.events.OrderCreatedEvent;
import com.app.core.api.order.domain.events.OrderItemCanceledEvent;
import com.app.core.api.order.domain.events.OrderItemCreatedEvent;
import com.app.core.api.order.domain.exceptions.OrderNotFoundException;
import com.app.core.api.order.domain.ports.out.OrderRepository;
import com.app.core.api.order.domain.service.OrderDiscountService;
import com.app.core.api.order.domain.service.OrderPaymentService;
import com.app.core.api.order.domain.service.OrderProductService;
import com.app.core.api.order.domain.service.OrderUserService;
import com.app.core.api.order.domain.vo.OrderStatus;
import com.app.core.api.shared.infrastructure.adapters.out.logging.CustomLogger;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementação do caso de uso de pedidos
 */
@Service
public class OrderService implements OrderUseCase {

    private final OrderRepository orderRepository;

    private final OrderDiscountService orderDiscountService;
    private final OrderProductService orderProductService;
    private final OrderPaymentService orderPaymentService;
    private final OrderUserService orderUserService;

    private final CreateOrderRequestMapper createOrderRequestMapper;
    private final OrderResponseMapper orderResponseMapper;
    private final OrderStatusResponseMapper orderStatusResponseMapper;

    private final ApplicationEventPublisher eventPublisher;
    private final CustomLogger logger;

    public OrderService(
            ApplicationEventPublisher eventPublisher,
            OrderRepository orderRepository,
            CreateOrderRequestMapper createOrderRequestMapper,
            OrderDiscountService orderDiscountService,
            OrderProductService orderProductService,
            OrderPaymentService orderPaymentService,
            OrderResponseMapper orderResponseMapper,
            OrderUserService orderUserService,
            OrderStatusResponseMapper orderStatusResponseMapper) {
        this.eventPublisher = eventPublisher;
        this.orderRepository = orderRepository;
        this.createOrderRequestMapper = createOrderRequestMapper;
        this.logger = CustomLogger.getLogger(getClass());
        this.orderDiscountService = orderDiscountService;
        this.orderProductService = orderProductService;
        this.orderPaymentService = orderPaymentService;
        this.orderUserService = orderUserService;
        this.orderResponseMapper = orderResponseMapper;
        this.orderStatusResponseMapper = orderStatusResponseMapper;
    }

    @Override
    @Transactional
    public OrderResponse createOrder(CreateOrderRequest createOrderRequest) {

        logger.info("Criando novo pedido para o cliente ID: {}", createOrderRequest.getUserId());

        var order = createOrderRequestMapper.toDomain(createOrderRequest);

        orderUserService.validateGuestCustomer(order);

        orderProductService.validateOrderItemProduct(order.getOrderItems());

        orderDiscountService.applyDiscount(order);

        var savedOrder = orderRepository.save(order);

        var saveOrderToResponse = orderResponseMapper.toResponse(savedOrder);

        var orderCreatedEvent = new OrderCreatedEvent();

        BeanUtils.copyProperties(saveOrderToResponse, orderCreatedEvent);
        List<OrderItemCreatedEvent> itemEvents = saveOrderToResponse.getItems().stream()
                .map(itemResponse -> {
                    OrderItemCreatedEvent itemEvent = new OrderItemCreatedEvent();
                    BeanUtils.copyProperties(itemResponse, itemEvent);
                    return itemEvent;
                })
                .toList();

        orderCreatedEvent.setItems(itemEvents);

        BeanUtils.copyProperties(saveOrderToResponse.getItems(), orderCreatedEvent.getItems());

        eventPublisher.publishEvent(orderCreatedEvent);

        logger.info("Pedido {} criado com sucesso. Total: {}", savedOrder.getId(), savedOrder.getAmount());

        return saveOrderToResponse;
    }

    @Override
    @Transactional
    public void updateOrderStatus(Long orderId, OrderStatus orderStatus) {
        logger.info("Atualizando status do pedido {} para {}", orderId, orderStatus);

        var order = orderRepository.findById(orderId);

        if (order.isEmpty()) {
            throw new OrderNotFoundException("Pedido", orderId);
        }
        else if (order.get().getOrderStatus() == orderStatus) {
            return;
        }

        order.get().setOrderStatus(orderStatus);

        var updatedOrder = orderRepository.save(order.get());

        logger.info("Status do pedido {} atualizado para {}", orderId, orderStatus);

        if (orderStatus == OrderStatus.CANCELLED) {
            var orderCanceledEvent = new OrderCanceledEvent();

            BeanUtils.copyProperties(updatedOrder, orderCanceledEvent);
            List<OrderItemCanceledEvent> itemEvents = updatedOrder.getOrderItems().stream()
                    .map(itemResponse -> {
                        var itemEvent = new OrderItemCanceledEvent();
                        BeanUtils.copyProperties(itemResponse, itemEvent);
                        return itemEvent;
                    })
                    .toList();

            orderCanceledEvent.setItems(itemEvents);

            BeanUtils.copyProperties(updatedOrder.getOrderItems(), orderCanceledEvent.getItems());

            logger.info("Publicando evento de ordem cancelada {}", orderId);

            eventPublisher.publishEvent(orderCanceledEvent);
        }
    }

    @Override
    @Transactional
    public OrderStatusResponse updateOrderStatus(Long orderId, OrderStatusRequest orderStatusRequest) {
        logger.info("Atualizando status do pedido {} para {}", orderId, orderStatusRequest.getOrderStatus());

        var order = orderRepository.findById(orderId);
        var newStatus = orderStatusRequest.getOrderStatus();

        if (order.isEmpty()) {
            throw new OrderNotFoundException("Pedido", orderId);
        }
        else if (order.get().getOrderStatus() == newStatus) {
            return orderStatusResponseMapper.toResponse(order.get());
        }

        order.get().setOrderStatus(newStatus);

        orderPaymentService.validateOrderPayment(order.get());

        var updatedOrder = orderRepository.save(order.get());
        var orderStatusToResponse = orderStatusResponseMapper.toResponse(updatedOrder);

        logger.info("Status do pedido {} atualizado para {}", orderId, newStatus);

        if (orderStatusRequest.getOrderStatus() == OrderStatus.CANCELLED) {
            var orderCanceledEvent = new OrderCanceledEvent();

            BeanUtils.copyProperties(updatedOrder, orderCanceledEvent);
            List<OrderItemCanceledEvent> itemEvents = updatedOrder.getOrderItems().stream()
                    .map(itemResponse -> {
                        var itemEvent = new OrderItemCanceledEvent();
                        BeanUtils.copyProperties(itemResponse, itemEvent);
                        return itemEvent;
                    })
                    .toList();

            orderCanceledEvent.setItems(itemEvents);

            BeanUtils.copyProperties(updatedOrder.getOrderItems(), orderCanceledEvent.getItems());

            logger.info("Publicando evento de ordem cancelada {}", orderId);

            eventPublisher.publishEvent(orderCanceledEvent);
        }

        return orderStatusToResponse;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getActiveOrdersSorted() {
        logger.info("Buscando pedidos ativos ordenados por prioridade e data de criação.");

        var activeOrders = orderRepository.findActiveOrdersSorted();

        return activeOrders.stream()
                .map(orderResponseMapper::toResponse)
                .toList();
    }
} 