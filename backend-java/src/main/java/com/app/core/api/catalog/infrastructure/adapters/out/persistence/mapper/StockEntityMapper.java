package com.app.core.api.catalog.infrastructure.adapters.out.persistence.mapper;

import com.app.core.api.catalog.domain.model.Stock;
import com.app.core.api.catalog.infrastructure.adapters.out.persistence.entity.StockEntity;
import com.app.core.api.shared.mapper.CycleAvoidingMappingContext;
import com.app.core.api.shared.mapper.DoIgnore;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * Mapper que converte entre a entidade de domínio Stock e a entidade JPA StockEntity
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StockEntityMapper {

    /**
     * Converte uma entidade JPA para uma entidade de domínio
     * @param entity Entidade JPA
     * @param cycleAvoidingMappingContext Contexto para evitar ciclos
     * @return Entidade de domínio
     */
    Stock toDomain(StockEntity entity, @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    /**
     * Converte uma lista de entidades JPA para uma lista de entidades de domínio
     * @param entities Lista de entidades JPA
     * @param cycleAvoidingMappingContext Contexto para evitar ciclos
     * @return Lista de entidades de domínio
     */
    List<Stock> toDomainList(List<StockEntity> entities, @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    /**
     * Converte uma entidade de domínio para uma entidade JPA
     * @param domain Entidade de domínio
     * @return Entidade JPA
     */
    StockEntity toEntity(Stock domain, @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @DoIgnore
    default Stock toDomain(StockEntity entity) {
        return toDomain(entity, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<Stock> toDomainList(List<StockEntity> entities) {
        return toDomainList(entities, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default StockEntity toEntity(Stock domain) {
        return toEntity(domain, new CycleAvoidingMappingContext());
    }
}