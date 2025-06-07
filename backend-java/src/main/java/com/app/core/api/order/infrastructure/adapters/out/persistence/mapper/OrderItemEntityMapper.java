package com.app.core.api.order.infrastructure.adapters.out.persistence.mapper;

import com.app.core.api.order.domain.model.OrderItem;
import com.app.core.api.order.infrastructure.adapters.out.persistence.entity.OrderItemEntity;
import com.app.core.api.order.infrastructure.adapters.out.persistence.mapper.shared.OrderNumberMapper;
import com.app.core.api.shared.mapper.CycleAvoidingMappingContext;
import com.app.core.api.shared.mapper.DoIgnore;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * Mapper que converte entre a entidade de domínio OrderItem e a entidade JPA OrderItemEntity
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {OrderNumberMapper.class})
public interface OrderItemEntityMapper {

    @Mapping(target = "order", ignore = true)
    OrderItem toDomain(OrderItemEntity entity, @Context CycleAvoidingMappingContext context);

    List<OrderItem> toDomainList(List<OrderItemEntity> entities, @Context CycleAvoidingMappingContext context);

    OrderItemEntity toEntity(OrderItem domain, @Context CycleAvoidingMappingContext context);

    @DoIgnore
    default OrderItem toDomain(OrderItemEntity entity) {
        return toDomain(entity, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<OrderItem> toDomainList(List<OrderItemEntity> entities) {
        return toDomainList(entities, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default OrderItemEntity toEntity(OrderItem domain) {
        return toEntity(domain, new CycleAvoidingMappingContext());
    }
}