package com.app.core.api.order.domain.model;

import com.app.core.api.order.domain.exceptions.OrderItemException;
import com.app.core.api.order.domain.vo.OrderItemPrice;
import com.app.core.api.shared.exception.BusinessException;
import com.app.core.api.shared.vo.AuditInfo;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidade de domínio que representa um item de pedido
 */
@Getter
@Setter
public class OrderItem {

    private Long id;
    private Long productId;
    private String name;
    private OrderItemPrice orderItemPrice;
    private String observations = "";

    private AuditInfo auditInfo = new AuditInfo();
    private Order order;

    /**
     * Construtor que cria um item de pedido com os dados informados
     *
     * @param productId        ID do produto
     * @param name   Nome do item do pedido
     * @param orderItemPrice   Preço do item
     * @param observations     Observações sobre o item
     * @throws NullPointerException se qualquer parâmetro for nulo
     */
    public OrderItem(
            Long productId,
            String name,
            OrderItemPrice orderItemPrice,
            String observations
    ) {
        validate(productId, name, orderItemPrice);

        this.productId = productId;
        this.name = name;
        this.orderItemPrice = orderItemPrice;
        this.observations = observations;
    }

    /**
     * Validação centralizada.
     *
     * @param productId        ID do produto
     * @param orderItemPrice   Preço do item
     * @param name   Nome do item do pedido
     * @throws NullPointerException se qualquer parâmetro for nulo
     */
    private void validate(
            Long productId,
            String name,
            OrderItemPrice orderItemPrice
    ) {
        Objects.requireNonNull(productId, "O ID do produto não pode ser nulo");
        Objects.requireNonNull(orderItemPrice, "O preço do item do pedido não pode ser nulo");

        if (name.isEmpty()) {
            throw new OrderItemException("O nome do item do pedido não pode ser nulo");
        }
    }

    /**
     * Retorna o subtotal do item de pedido com base no preço e quantidade
     *
     * @return subtotal do item
     */
    BigDecimal getSubTotal () {
        return this.orderItemPrice.getSubTotal();
    }

    /**
     * Atualiza o campo updatedAt com o horário atual.
     *
     * @throws BusinessException se o horário atual for menor ou igual ao createdAt
     */
    void markUpdatedNow() {
        this.auditInfo.setUpdatedAt(LocalDateTime.now());
    }

    /**
     * Retorna o preço do item do pedido
     *
     */
    public BigDecimal getPrice() {
        return orderItemPrice.unitPrice();
    }

    /**
     * Retorna a quantidade do item do pedido
     *
     */
    public Integer getQuantity() {return orderItemPrice.quantity();}
}