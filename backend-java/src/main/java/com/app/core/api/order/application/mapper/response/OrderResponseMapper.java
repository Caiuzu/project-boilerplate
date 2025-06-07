package com.app.core.api.order.application.mapper.response;

import com.app.core.api.order.application.dto.response.OrderResponse;
import com.app.core.api.order.domain.model.Order;
import com.app.core.api.order.domain.vo.OrderStatus;
import com.app.core.api.shared.mapper.AuditInfoMapper;
import com.app.core.api.shared.mapper.CycleAvoidingMappingContext;
import com.app.core.api.shared.mapper.DoIgnore;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * Mapper que converte a entidade {@link Order} para o DTO {@link OrderResponse}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {OrderItemResponseMapper.class, AuditInfoMapper.class})
public interface OrderResponseMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "orderNumber", target = "orderNumber")
    @Mapping(source = "orderStatus", target = "status")
    @Mapping(source = "orderStatus", target = "statusDescription", qualifiedByName = "mapStatusToDescription")
    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "amount", target = "totalAmount")
    @Mapping(source = "orderItems", target = "items")
    @Mapping(source = "auditInfo", target = "createdAt", qualifiedByName = "mapCreatedAt")
    @Mapping(source = "auditInfo", target = "updatedAt", qualifiedByName = "mapUpdatedAt")
    OrderResponse toResponse(Order order, @Context CycleAvoidingMappingContext context);

    @Mapping(source = "orderStatus", target = "statusDescription", qualifiedByName = "mapStatusToDescription")
    @Mapping(source = "auditInfo", target = "createdAt", qualifiedByName = "mapCreatedAt")
    @Mapping(source = "auditInfo", target = "updatedAt", qualifiedByName = "mapUpdatedAt")
    List<OrderResponse> toResponseList(List<Order> orders, @Context CycleAvoidingMappingContext context);

    @DoIgnore
    default OrderResponse toResponse(Order order) {
        return toResponse(order, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<OrderResponse> toResponseList(List<Order> orders) {
        return toResponseList(orders, new CycleAvoidingMappingContext());
    }

    @Named("mapStatusToDescription")
    default String mapStatusToDescription(OrderStatus status) {
        if (status == null) return null;
        return switch (status) {
            case RECEIVED -> OrderStatus.RECEIVED.getDescription();
            case PREPARING -> OrderStatus.PREPARING.getDescription();
            case READY -> OrderStatus.READY.getDescription();
            case COMPLETED -> OrderStatus.COMPLETED.getDescription();
            case CANCELLED -> OrderStatus.CANCELLED.getDescription();
        };
    }
}