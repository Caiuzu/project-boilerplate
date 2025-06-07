package com.app.core.api.catalog.infrastructure.adapters.out.persistence.mapper;

import com.app.core.api.catalog.domain.model.Catalog;
import com.app.core.api.catalog.infrastructure.adapters.out.persistence.entity.CatalogEntity;
import com.app.core.api.shared.mapper.CycleAvoidingMappingContext;
import com.app.core.api.shared.mapper.DoIgnore;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * Mapper que converte entre a entidade de domínio Catalog e a entidade JPA CatalogEntity
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {CategoryEntityMapper.class})
public interface CatalogEntityMapper {

    /**
     * Converte uma entidade JPA para uma entidade de domínio
     * @param entity Entidade JPA
     * @param cycleAvoidingMappingContext Contexto para evitar ciclos
     * @return Entidade de domínio
     */
    Catalog toDomain(CatalogEntity entity, @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    /**
     * Converte uma lista de entidades JPA para uma lista de entidades de domínio
     * @param entities Lista de entidades JPA
     * @param cycleAvoidingMappingContext Contexto para evitar ciclos
     * @return Lista de entidades de domínio
     */
    List<Catalog> toDomainList(List<CatalogEntity> entities, @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    /**
     * Converte uma entidade de domínio para uma entidade JPA
     * @param domain Entidade de domínio
     * @return Entidade JPA
     */
    CatalogEntity toEntity(Catalog domain, @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @DoIgnore
    default Catalog toDomain(CatalogEntity entity) {
        return toDomain(entity, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<Catalog> toDomainList(List<CatalogEntity> entities) {
        return toDomainList(entities, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default CatalogEntity toEntity(Catalog domain) {
        return toEntity(domain, new CycleAvoidingMappingContext());
    }
}