package com.app.core.api.order.infrastructure.adapters.out.persistence.mapper;

import com.app.core.api.order.domain.model.Order;
import com.app.core.api.order.infrastructure.adapters.out.persistence.entity.OrderEntity;
import com.app.core.api.order.infrastructure.adapters.out.persistence.mapper.shared.OrderNumberMapper;
import com.app.core.api.shared.mapper.CycleAvoidingMappingContext;
import com.app.core.api.shared.mapper.DoIgnore;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * Mapper que converte entre a entidade de domínio Order e a entidade JPA OrderEntity
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {
        OrderItemEntityMapper.class,
        OrderNumberMapper .class
})
public interface OrderEntityMapper {

    Order toDomain(OrderEntity entity, @Context CycleAvoidingMappingContext context);

    List<Order> toDomainList(List<OrderEntity> entities, @Context CycleAvoidingMappingContext context);

    OrderEntity toEntity(Order domain, @Context CycleAvoidingMappingContext context);

    @DoIgnore
    default Order toDomain(OrderEntity entity) {
        return toDomain(entity, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<Order> toDomainList(List<OrderEntity> entities) {
        return toDomainList(entities, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default OrderEntity toEntity(Order domain) {
        return toEntity(domain, new CycleAvoidingMappingContext());
    }
}