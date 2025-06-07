package com.app.core.api.payment.infrastructure.adapters.out.persistence.repository;

import com.app.core.api.payment.domain.model.Payment;
import com.app.core.api.payment.domain.ports.out.PaymentRepository;
import com.app.core.api.payment.infrastructure.adapters.out.persistence.mapper.PaymentEntityMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Adaptador que implementa a interface do repositório de pagamentos
 * Faz a ponte entre o domínio e a infraestrutura de persistência
 */
@Component
public class PaymentRepositoryAdapter implements PaymentRepository {

    private final SpringDataPaymentRepository repository;
    private final PaymentEntityMapper mapper;

    public PaymentRepositoryAdapter(SpringDataPaymentRepository repository, PaymentEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Payment save(Payment payment) {
        var entity = mapper.toEntity(payment);
        var savedEntity = repository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Payment> findTopByOrderIdOrderByIdDesc(Long orderId) {
        return repository.findTopByOrderIdOrderByIdDesc(orderId)
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsByOrderId(Long orderId) {
        return repository.existsByOrderId(orderId);
    }

    @Override
    public List<Payment> findExpiredPaymentsWithoutApprovedOrCancelled(LocalDateTime now) {
      var paymentEntities = repository.findExpiredPaymentsWithoutApprovedOrCancelled(now);

      return mapper.toDomainList(paymentEntities);
    }
}