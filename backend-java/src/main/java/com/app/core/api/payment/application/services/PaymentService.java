package com.app.core.api.payment.application.services;

import com.app.core.api.order.domain.events.OrderCreatedEvent;
import com.app.core.api.order.domain.exceptions.OrderNotFoundException;
import com.app.core.api.payment.application.dto.request.AcquirerNotificationRequest;
import com.app.core.api.payment.application.dto.response.AcquirerOrderResponse;
import com.app.core.api.payment.application.dto.response.PaymentStatusResponse;
import com.app.core.api.payment.application.dto.response.QrCodeResponse;
import com.app.core.api.payment.application.mapper.request.GenerateQrCodeRequestMapper;
import com.app.core.api.payment.application.mapper.response.PaymentStatusResponseMapper;
import com.app.core.api.payment.application.mapper.response.QrCodeResponseMapper;
import com.app.core.api.payment.application.ports.in.PaymentUseCase;
import com.app.core.api.payment.application.ports.out.AcquirerPort;
import com.app.core.api.payment.domain.events.PaymentApprovedEvent;
import com.app.core.api.payment.domain.events.PaymentExpiredEvent;
import com.app.core.api.payment.domain.events.PaymentInitializationErrorEvent;
import com.app.core.api.payment.domain.exceptions.PaymentNotFoundException;
import com.app.core.api.payment.domain.model.Payment;
import com.app.core.api.payment.domain.ports.out.PaymentRepository;
import com.app.core.api.payment.domain.vo.PaymentStatus;
import com.app.core.api.shared.application.ports.in.AccessManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Implementação do caso de uso de pagamento
 */
@Service
@Slf4j
public class PaymentService implements PaymentUseCase {

    private final PaymentRepository paymentRepository;
    private final AcquirerPort acquirerPort;
    private final GenerateQrCodeRequestMapper generateQrCodeRequestMapper;
    private final PaymentStatusResponseMapper paymentStatusResponseMapper;
    private final QrCodeResponseMapper qrCodeResponseMapper;
    private final ApplicationEventPublisher eventPublisher;
    private final AccessManager accessManager;

    public PaymentService(
            PaymentRepository paymentRepository,
            AcquirerPort acquirerPort,
            GenerateQrCodeRequestMapper generateQrCodeRequestMapper,
            ApplicationEventPublisher eventPublisher,
            PaymentStatusResponseMapper paymentStatusResponseMapper,
            QrCodeResponseMapper qrCodeResponseMapper,
            AccessManager accessManager) {
        this.paymentRepository = paymentRepository;
        this.acquirerPort = acquirerPort;
        this.eventPublisher = eventPublisher;
        this.paymentStatusResponseMapper = paymentStatusResponseMapper;
        this.generateQrCodeRequestMapper = generateQrCodeRequestMapper;
        this.qrCodeResponseMapper = qrCodeResponseMapper;
        this.accessManager = accessManager;
    }

    @Override
    @Transactional
    public void initializePayment(OrderCreatedEvent event) {

        try {
            log.info("Inicializando pagamento para o pedido {} no valor de {}", event.getId(), event.getTotalAmount());

            if (paymentRepository.existsByOrderId(event.getId())) {
                log.info("Pagamento já existe para o pedido {}", event.getId());
                return;
            }

            var generateQrCodeBody = generateQrCodeRequestMapper.toRequest(event);
            var payment = new Payment(
                    event.getUserId(),
                    event.getId(),
                    generateQrCodeBody.getTotal_amount()
            );
            generateQrCodeBody.setExpiration_date(payment.getLaPazISO8601ExpiresIn());

            var generateQrCodeResponse = acquirerPort.generateQrCode(generateQrCodeBody);

            payment.setQrCode(generateQrCodeResponse.getQr_data());

            var savedPayment = paymentRepository.save(payment);

            log.info("Pagamento inicializado com ID: {}, qrCode: {}, pedido: {}",
                    savedPayment.getId(), savedPayment.getQrCode(), savedPayment.getOrderId());

        }
        catch (Exception ex) {

            log.warn("Erro na inicialização do pagamento. Pedido: {}, Causa: {}", event.getId(), ex.getMessage());

            var paymentInitializationErrorEvent = new PaymentInitializationErrorEvent(event.getId(), ex.getMessage());
            eventPublisher.publishEvent(paymentInitializationErrorEvent);

            throw  ex;
        }
    }

    @Override
    @Transactional
    public void processPaymentNotification(AcquirerNotificationRequest acquirerNotificationRequest) {

        var acquirerPaymentResponse = acquirerPort.getAcquirerPayments(acquirerNotificationRequest.getDataId());
        var longExternalRefernce = Long.parseLong(acquirerPaymentResponse.getExternal_reference());
        var payment = paymentRepository.findTopByOrderIdOrderByIdDesc(longExternalRefernce);

        if (payment.isEmpty()) {
            log.warn("Pagamento não foi encontrado a partir da external_reference! {}", longExternalRefernce);
            throw new PaymentNotFoundException("Pagamento", longExternalRefernce);
        }
        else if(payment.get().getStatus() == PaymentStatus.APPROVED) {
            log.info("Pagamento não foi encontrado a partir da external_reference! {}", longExternalRefernce);
            return;
        }
        // Indica que se trata de uma segunda tentativa de pagamento
        else if(payment.get().getStatus() != PaymentStatus.PENDING) {
            payment.get().setId(null);
        }

        payment.get().setStatus(acquirerPaymentResponse.getStatus());
        payment.get().setType(acquirerPaymentResponse.getPaymentType());
        payment.get().setTid(acquirerNotificationRequest.getDataId());
        paymentRepository.save(payment.get());

        if (acquirerPaymentResponse.getStatus() == PaymentStatus.APPROVED) {
            log.info("Pagamento aprovado: {}, Publicando evento!", payment.get().getId());

            eventPublisher.publishEvent(
                PaymentApprovedEvent.of(
                    payment.get().getId(),
                    payment.get().getOrderId(),
                    payment.get().getAmount(),
                    payment.get().getTypeName()
                )
            );

            log.info("Evento de pagamento aprovado publicado! PaymentId: {}, OrderId: {}!", payment.get().getId(), payment.get().getOrderId());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentStatusResponse getOrderPaymentStatus(Long orderId) {
        var payment = paymentRepository.findTopByOrderIdOrderByIdDesc(orderId);

        if (payment.isEmpty()) {
            log.warn("Pagamento não foi encontrado a partir do orderId! {}", orderId);
            throw new PaymentNotFoundException("Pagamento", orderId);
        }

        accessManager.validateAccess(payment.get().getUserId());


        return paymentStatusResponseMapper.toResponse(payment.get());
    }

    @Override
    @Transactional
    public void processExpiredPayments() {

        var expiredPayments = paymentRepository.findExpiredPaymentsWithoutApprovedOrCancelled(LocalDateTime.now());

        if (expiredPayments.isEmpty()) {
            log.info("Nenhum pagamento pendente expirado encontrado!");
            return;
        }

        for (Payment expiredPayment: expiredPayments) {
            log.info("Iniciando processamento expirado: {}, pedido: {}, expirado em: {}", expiredPayment.getId(), expiredPayment.getOrderId(), expiredPayment.getExpiresIn().toString());
            expiredPayment.setStatus(PaymentStatus.CANCELLED);
            paymentRepository.save(expiredPayment);

            log.info("Pagamento expirado cancelado: {}", expiredPayment.getId());

            var expiredEvent = new PaymentExpiredEvent(expiredPayment.getId(), expiredPayment.getOrderId(), expiredPayment.getExpiresIn());
            eventPublisher.publishEvent(expiredEvent);

            log.info("Evento de pagamento expirado publicado: {}", expiredPayment.getId());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public QrCodeResponse getOrderPaymentQrCode(Long orderId) {
        var payment = paymentRepository.findTopByOrderIdOrderByIdDesc(orderId);

        if (payment.isEmpty()) {
            log.warn("Pagamento não foi encontrado a partir do orderId! {}", orderId);
            throw new PaymentNotFoundException("Pagamento", orderId);
        }

        accessManager.validateAccess(payment.get().getUserId());

        return qrCodeResponseMapper.toResponse(payment.get());
    }

    @Override
    @Transactional(readOnly = true)
    public AcquirerOrderResponse getAcquirerOrder(Long merchantOrder) {
        var order = acquirerPort.getAcquirerOrder(merchantOrder);

        if (order == null) {
            log.warn("Pedido não foi encontrado no adquirente! Merchant Order: {}", merchantOrder);
            throw new OrderNotFoundException("Pedido adquirente", merchantOrder);
        }

        return order;
    }
}