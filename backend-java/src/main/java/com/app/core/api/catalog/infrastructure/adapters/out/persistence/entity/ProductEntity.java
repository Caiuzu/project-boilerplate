package com.app.core.api.catalog.infrastructure.adapters.out.persistence.entity;

import com.app.core.api.catalog.domain.vo.Details;
import com.app.core.api.catalog.domain.vo.ImageUrl;
import com.app.core.api.shared.vo.AuditInfo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Entidade JPA para produto
 */
@Entity
@Table(name = "products", uniqueConstraints = {
        @UniqueConstraint(name = "un_product_category", columnNames = {"name", "category_id"})
})
@Getter
@Setter
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id", foreignKey = @ForeignKey(name = "fk_product_category"))
    private CategoryEntity category;

    @Embedded
    private Details details;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Embedded
    private ImageUrl imageUrl;

    @Column(name = "display_order")
    private Integer displayOrder;

    @Column(nullable = false)
    private Boolean active = true;

    @Embedded
    private AuditInfo auditInfo = new AuditInfo();

    @OneToOne(mappedBy = "product",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH},
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private StockEntity stock;
}