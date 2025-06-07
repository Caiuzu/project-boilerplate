package com.app.core.api.order.domain.model;

import com.app.core.api.order.domain.exceptions.OrderException;
import com.app.core.api.order.domain.vo.OrderNumber;
import com.app.core.api.order.domain.vo.OrderStatus;
import com.app.core.api.shared.exception.BusinessException;
import com.app.core.api.shared.vo.AuditInfo;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Entidade de domínio que representa um pedido
 * AGGREGATE ROOT:
 *  - Toda modificação de entidades internas do agregado devem passar pela entidade raíz;
 *  - Único ponto de entrada para qualquer entidade interna do agregado (Lei de Demeter);
 *  - Entidades dentro deste agregado podem se referenciar via id ou objeto;
 *  - Entidades de outros agregados só podem referenciar esta entidade raiz, e isto deve ser via Id;
 */
@Data
public class Order {

    private Long id;
    private Long userId;
    private OrderNumber orderNumber = new OrderNumber(LocalDate.now().getYear(), (id == null ) ? 0 : id);
    private OrderStatus orderStatus = OrderStatus.RECEIVED;
    private BigDecimal amount;
    private AuditInfo auditInfo = new AuditInfo();

    private List<OrderItem> orderItems = new ArrayList<>();

    /**
     * Construtor que cria uma nova instância de pedido com os dados fornecidos.
     *
     * @param userId   ID do cliente que realizou o pedido
     * @param orderItems   Lista de itens do pedido
     * @throws NullPointerException     se userId, orderNumber, orderStatus ou amount forem nulos
     * @throws OrderException se orderItems for vazio ou se o valor calculado do pedido for menor ou igual a zero
     */
    public Order(
            Long userId,
            List<OrderItem> orderItems
    ) {
        validate(orderItems);
        this.userId = userId; // Validado via serviço de domínio

        for (OrderItem orderItem : orderItems) {
            addItem(orderItem);
        }
    }

    /**
     * Validação centralizada.
     *
     * @param orderItems   Lista de itens do pedido
     * @throws NullPointerException     se qualquer parâmetro obrigatório for nulo
     * @throws OrderException se a lista de itens estiver vazia
     */
    private void validate(
            List<OrderItem> orderItems
    ) {
        Objects.requireNonNull(orderItems, "A lista de itens do pedido não pode ser nula");

        if (orderItems.isEmpty()) {
            throw new OrderException("O pedido deve conter itens");
        }
    }

    /**
     * Obtém o ID do cliente (se disponível)
     * @return ID do cliente ou null se não houver cliente associado
     */
    public Long getUserId() {
        return userId != null ? userId : null;
    }

    /**
     * Obtém o número do pedido
     * @return o número do pedido
     */
    public String getOrderNumber() {
        return this.orderNumber.getFormatted();
    }

    /**
     * Fornece uma lista imutável de itens do pedido
     * @return lista imutável de itens
     */
    public List<OrderItem> getOrderItems() {
        return Collections.unmodifiableList(this.orderItems);
    }

    /**
     * Adiciona um item ao pedido
     * @param item Item a ser adicionado
     * @throws NullPointerException se o item do pedido for nulo
     */
    public void addItem(OrderItem item) {
        Objects.requireNonNull(item, "O item do pedido não pode ser nulo");

        item.setOrder(this);
        orderItems.add(item);
        calculateTotalAmount();
    }

    /**
     * Remove um item do pedido
     * @param item Item a ser removido
     * @throws NullPointerException se o item do pedido for nulo
     */
    public void removeItem(OrderItem item) {
        Objects.requireNonNull(item, "O item do pedido não pode ser nulo");

        if (orderItems != null) {
            orderItems.remove(item);
            calculateTotalAmount();
        }
    }

    /**
     * Calcula o valor total do pedido
     * @throws OrderException se o valor do pedido for menor ou igual à 0
     */
    private void calculateTotalAmount() {
        amount = orderItems.stream()
                .map(OrderItem::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new OrderException(
                    "O valor do pedido deve ser maior que 0"
            );
        }
    }

    /**
     * Atualiza o status do pedido
     * @param newStatus Novo status
     * @throws NullPointerException se o status do pedido for nulo
     * @throws OrderException se a transição de status não for permitida
     */
    public void setOrderStatus(OrderStatus newStatus) {
        Objects.requireNonNull(newStatus, "O status do pedido não pode ser nulo");

        if (this.orderStatus == newStatus) {
            return;
        }

        validateStatusTransition(newStatus);

        this.orderStatus = newStatus;
        markUpdatedNow();
    }

    /**
     * Valida se a transição de status é permitida
     * @param newStatus Novo status a ser validado
     * @throws NullPointerException se o status do pedido for nulo
     * @throws OrderException se a transição não for permitida
     */
    private void validateStatusTransition(OrderStatus newStatus) {
        Objects.requireNonNull(newStatus, "O status do pedido não pode ser nulo");

        if (this.orderStatus == OrderStatus.CANCELLED) {
            throw new OrderException(
                    "Não é possível alterar o status de um pedido cancelado"
            );
        }
        else if (this.orderStatus == OrderStatus.COMPLETED) {
            throw new OrderException(
                    "Não é possível alterar o status de um pedido já entregue ao cliente"
            );
        }
        else if (this.orderStatus != OrderStatus.RECEIVED && newStatus == OrderStatus.CANCELLED) {
            throw new OrderException(
                    "Não é possível alterar o status de um pedido para cancelado após o início do seu preparo"
            );
        }
        else if (newStatus == OrderStatus.RECEIVED) {
            throw new OrderException(
                    String.format("Não é possível retornar o status da ordem para %s. Revise o fluxo: %s -> %s -> %s...",
                            OrderStatus.RECEIVED, OrderStatus.RECEIVED, OrderStatus.PREPARING, OrderStatus.READY)
            );
        }
    }

    /**
     * Aplica um desconto percentual ao valor do pagamento.
     *
     * @param percent o percentual de desconto a ser aplicado (1 a 95)
     * @throws OrderException se percent não estiver entre 1 e 95
     */
    public void applyDiscount(int percent) {
        if (percent < 1 || percent > 95) {
            throw new OrderException("O percentual de desconto deve estar entre 1 e 95");
        }

        BigDecimal percentage = BigDecimal.valueOf(percent).divide(BigDecimal.valueOf(95));
        BigDecimal discount = this.amount.multiply(percentage);
        this.amount = this.amount.subtract(discount);
    }

    /**
     * Atualiza o campo updatedAt com o horário atual.
     *
     * @throws BusinessException se o horário atual for menor ou igual ao createdAt
     */
    public void markUpdatedNow() {
        this.auditInfo.setUpdatedAt(LocalDateTime.now());
    }
}