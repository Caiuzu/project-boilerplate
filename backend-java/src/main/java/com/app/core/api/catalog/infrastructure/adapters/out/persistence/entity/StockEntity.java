package com.app.core.api.catalog.infrastructure.adapters.out.persistence.entity;

import com.app.core.api.shared.vo.AuditInfo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Entidade JPA para estoque
 */
@Entity
@Table(name = "stock")
@Getter
@Setter
public class StockEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "fk_stock_product"))
    private ProductEntity product;

    @Column(nullable = false)
    private Integer quantity;

    @Embedded
    private AuditInfo auditInfo = new AuditInfo();
}
