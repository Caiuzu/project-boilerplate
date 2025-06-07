package com.app.core.api.payment.infrastructure.adapters.out.persistence.repository;

import com.app.core.api.payment.infrastructure.adapters.out.persistence.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repositório Spring Data JPA para a entidade PaymentEntity
 */
@Repository
public interface SpringDataPaymentRepository extends JpaRepository<PaymentEntity, Long> {
    /**
     * Busca o último pagamento inserido para um determinado pedido
     */
    Optional<PaymentEntity> findTopByOrderIdOrderByIdDesc(Long orderId);

    /**
     * Verifica se já existe um pagamento para o pedido
     */
    boolean existsByOrderId(Long orderId);

    /**
     * Busca pagamentos não aprovados expirados
     */
    @Query("""
    SELECT p
    FROM PaymentEntity p
    WHERE p.id IN (
        SELECT MAX(p2.id)
        FROM PaymentEntity p2
        WHERE p2.expiresIn < :now
          AND NOT EXISTS (
              SELECT 1
              FROM PaymentEntity p3
              WHERE p3.orderId = p2.orderId
                AND CAST(p3.status AS string) IN ('APPROVED', 'CANCELLED')
          )
        GROUP BY p2.orderId
    )
    """)
    List<PaymentEntity> findExpiredPaymentsWithoutApprovedOrCancelled(LocalDateTime now);
} 