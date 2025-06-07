package com.app.core.api.catalog.infrastructure.adapters.out.persistence.entity;

import com.app.core.api.catalog.domain.vo.Details;
import com.app.core.api.catalog.domain.vo.ImageUrl;
import com.app.core.api.shared.vo.AuditInfo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidade JPA para categoria
 */
@Entity
@Table(name = "categories", uniqueConstraints = {
        @UniqueConstraint(name = "un_category_catalog", columnNames = {"name", "catalog_id"})
})
@Getter
@Setter
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "catalog_id", foreignKey = @ForeignKey(name = "fk_category_catalog"))
    private CatalogEntity catalog;

    @Embedded
    private Details details;

    @Embedded
    private ImageUrl imageUrl;

    @Column(name = "display_order")
    private Integer displayOrder;

    @Column(nullable = false)
    private Boolean active = true;

    @Embedded
    private AuditInfo auditInfo = new AuditInfo();

    @OneToMany(mappedBy = "category",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH},
            orphanRemoval = true)
    private List<ProductEntity> products = new ArrayList<>();

}