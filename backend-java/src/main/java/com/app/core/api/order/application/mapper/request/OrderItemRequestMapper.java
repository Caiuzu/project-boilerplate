package com.app.core.api.order.application.mapper.request;

import com.app.core.api.order.application.dto.request.OrderItemRequest;
import com.app.core.api.order.domain.model.OrderItem;
import com.app.core.api.order.domain.vo.OrderItemPrice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper responsável por converter {@link OrderItemRequest} em {@link OrderItem}.
 */
@Mapper(componentModel = "spring")
public interface OrderItemRequestMapper {

    /**
     * Converte um {@link OrderItemRequest} para um {@link OrderItem}.
     *
     * @param dto DTO com os dados do item do pedido.
     * @return instância da entidade {@link OrderItem}.
     */
    @Mapping(target = "orderItemPrice", source = "dto", qualifiedByName = "toOrderItemPrice")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "auditInfo", ignore = true)
    OrderItem toDomain(OrderItemRequest dto);

    /**
     * Cria um {@link OrderItemPrice} a partir de um {@link OrderItemRequest}.
     *
     * @param dto DTO com os dados do item do pedido.
     * @return Value Object {@link OrderItemPrice}.
     */
    @Named("toOrderItemPrice")
    default OrderItemPrice toOrderItemPrice(OrderItemRequest dto) {
        return new OrderItemPrice(dto.getQuantity(), dto.getUnitPrice());
    }
}