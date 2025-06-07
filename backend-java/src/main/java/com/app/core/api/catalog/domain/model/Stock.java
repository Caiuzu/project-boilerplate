package com.app.core.api.catalog.domain.model;

import com.app.core.api.catalog.domain.exceptions.StockException;
import com.app.core.api.shared.exception.BusinessException;
import com.app.core.api.shared.vo.AuditInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Entidade de domínio que representa o estoque de um produto.
 */
@Getter
@Setter
@NoArgsConstructor
public class Stock {

    private Long id;
    private Integer quantity = 0;
    private AuditInfo auditInfo = new AuditInfo();

    private Product product;

    /**
     * Construtor que define a quantidade inicial do estoque.
     *
     * @param quantity quantidade inicial de itens no estoque
     * @throws StockException se a quantidade for menor que zero
     */
    public Stock(int quantity) {
        validate(quantity);
        this.quantity = quantity;
    }

    /**
     * Valida a quantidade informada para o estoque.
     *
     * @param quantity quantidade a ser validada
     * @throws StockException se a quantidade for menor ou igual a zero
     */
    private void validate(int quantity) {
        if (quantity < 0) {
            throw new StockException("A quantidade de estoque deve ser positiva");
        }
    }

    /**
     * Define a quantidade de itens no estoque.
     *
     * @param quantity nova quantidade a ser atribuída
     * @throws StockException se a quantidade for menor ou igual a zero
     */
    public void setQuantity(int quantity) {
        if (quantity < 0) {
            throw new StockException("A quantidade de estoque deve ser positiva");
        }
        this.quantity = quantity;
    }

    /**
     * Atualiza o campo updatedAt com o horário atual.
     *
     * @throws BusinessException se o horário atual for menor ou igual ao createdAt
     */
    void markUpdatedNow() {
        this.auditInfo.setUpdatedAt(LocalDateTime.now());
    }
}