package com.app.core.api.order.infrastructure.adapters.out.persistence.entity;

import com.app.core.api.order.domain.vo.OrderNumber;
import com.app.core.api.order.domain.vo.OrderStatus;
import com.app.core.api.order.infrastructure.adapters.out.persistence.converter.OrderNumberConverter;
import com.app.core.api.shared.vo.AuditInfo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hibernate.type.SqlTypes.NAMED_ENUM;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class OrderEntity {

    // GenerationType IDENTITY não acionará o @PrePersist de criação do orderNumber
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_id_seq_gen")
    @SequenceGenerator(name = "order_id_seq_gen", sequenceName = "orders_id_seq", allocationSize = 1)
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Convert(converter = OrderNumberConverter.class)
    @Column(name = "order_number", length = 20, nullable = false, unique = true)
    private OrderNumber orderNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "order_status_enum ")
    @JdbcTypeCode(NAMED_ENUM)
    private OrderStatus orderStatus = OrderStatus.RECEIVED;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Embedded
    private AuditInfo auditInfo = new AuditInfo();

    @OneToMany(mappedBy = "order",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH},
            orphanRemoval = true)
    private List<OrderItemEntity> orderItems = new ArrayList<>();

    @PrePersist
    public void generateOrderNumber() {
        this.orderNumber = new OrderNumber(LocalDate.now().getYear(), this.id.intValue());
    }
}