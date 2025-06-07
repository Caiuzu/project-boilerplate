package com.app.core.api.payment.application.ports.in;

import com.app.core.api.order.domain.events.OrderCreatedEvent;
import com.app.core.api.payment.application.dto.request.AcquirerNotificationRequest;
import com.app.core.api.payment.application.dto.response.AcquirerOrderResponse;
import com.app.core.api.payment.application.dto.response.PaymentStatusResponse;
import com.app.core.api.payment.application.dto.response.QrCodeResponse;

/**
 * Interface do caso de uso de pagamento
 */
public interface PaymentUseCase {
    
    /**
     * Inicializa um pagamento para um pedido
     *
     * @param orderCreatedEvent evento de criação de pedido
     */
    void initializePayment(OrderCreatedEvent orderCreatedEvent);

    /**
     * Gerencia notificação de pagamento adquirente
     *
     * @param acquirerNotificationRequest notificação de pagamento adquirente
     */
    void processPaymentNotification(AcquirerNotificationRequest acquirerNotificationRequest);

    /**
     * Retorna o status de pagamento de um pedido
     *
     * @param orderId id do pedido
     */
    PaymentStatusResponse getOrderPaymentStatus(Long orderId);

    /**
     * Processa pagamentos não aprovados expirados
     *
     */
    void processExpiredPayments();

    /**
     * Retorna o qr code de pagamento de um dado pedido
     *
     * @param orderId id do pedido
     */
     QrCodeResponse getOrderPaymentQrCode(Long orderId);

    /**
     * Retorna um pedido no adquirente a partir de um id
     *
     * @param merchantOrder id do merchant order
     */
     AcquirerOrderResponse getAcquirerOrder(Long merchantOrder);
} 