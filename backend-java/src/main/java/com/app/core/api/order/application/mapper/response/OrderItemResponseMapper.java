package com.app.core.api.order.application.mapper.response;

import com.app.core.api.order.application.dto.response.OrderItemResponse;
import com.app.core.api.order.domain.model.OrderItem;
import com.app.core.api.order.domain.vo.OrderItemPrice;
import com.app.core.api.shared.mapper.AuditInfoMapper;
import com.app.core.api.shared.mapper.CycleAvoidingMappingContext;
import com.app.core.api.shared.mapper.DoIgnore;
import org.mapstruct.*;

import java.util.List;

/**
 * Mapper que converte a entidade {@link OrderItem} para o DTO {@link OrderItemResponse}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {AuditInfoMapper.class})
public interface OrderItemResponseMapper {

    @Mapping(target = "quantity", source = "orderItemPrice", qualifiedByName = "mapQuantity")
    @Mapping(target = "unitPrice", source = "orderItemPrice", qualifiedByName = "mapUnitPrice")
    @Mapping(target = "subtotal", source = "orderItemPrice", qualifiedByName = "mapSubtotal")
    @Mapping(source = "auditInfo", target = "createdAt", qualifiedByName = "mapCreatedAt")
    @Mapping(source = "auditInfo", target = "updatedAt", qualifiedByName = "mapUpdatedAt")
    OrderItemResponse toResponse(OrderItem orderItem, @Context CycleAvoidingMappingContext context);

    @Mapping(source = "auditInfo", target = "createdAt", qualifiedByName = "mapCreatedAt")
    @Mapping(source = "auditInfo", target = "updatedAt", qualifiedByName = "mapUpdatedAt")
    List<OrderItemResponse> toResponseList(List<OrderItem> orderItems, @Context CycleAvoidingMappingContext context);

    @DoIgnore
    default OrderItemResponse toResponse(OrderItem orderItem) {
        return toResponse(orderItem, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<OrderItemResponse> toResponseList(List<OrderItem> orderItems) {
        return toResponseList(orderItems, new CycleAvoidingMappingContext());
    }

    @Named("mapQuantity")
    static Integer mapQuantity(OrderItemPrice price) {
        return price != null ? price.quantity() : null;
    }

    @Named("mapUnitPrice")
    static java.math.BigDecimal mapUnitPrice(OrderItemPrice price) {
        return price != null ? price.unitPrice() : null;
    }

    @Named("mapSubtotal")
    static java.math.BigDecimal mapSubtotal(OrderItemPrice price) {
        return price != null ? price.getSubTotal() : null;
    }
}