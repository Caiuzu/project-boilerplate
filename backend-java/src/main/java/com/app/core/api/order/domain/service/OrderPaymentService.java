package com.app.core.api.order.domain.service;

import com.app.core.api.order.domain.model.Order;
import com.app.core.api.order.domain.vo.OrderStatus;
import com.app.core.api.payment.domain.exceptions.PaymentNotFoundException;
import com.app.core.api.payment.domain.ports.out.PaymentRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

/**
 * Serviço de domínio responsável pela validação do pagamento de um pedido.
 */
@Data
@Service
public class OrderPaymentService {

    private final PaymentRepository paymentRepository;

    public OrderPaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public void validateOrderPayment(Order order) {

        var payment = paymentRepository.findTopByOrderIdOrderByIdDesc(order.getId());

        if (payment.isEmpty() && order.getOrderStatus() != OrderStatus.RECEIVED) {
            throw new PaymentNotFoundException("O pagamento do pedido não existe");
        }
    }
}