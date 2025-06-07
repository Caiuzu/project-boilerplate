package com.app.core.api.catalog.infrastructure.adapters.out.persistence.mapper;

import com.app.core.api.catalog.domain.model.Category;
import com.app.core.api.catalog.infrastructure.adapters.out.persistence.entity.CategoryEntity;
import com.app.core.api.shared.mapper.CycleAvoidingMappingContext;
import com.app.core.api.shared.mapper.DoIgnore;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * Mapper que converte entre a entidade de domínio Category e a entidade JPA CategoryEntity
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {ProductEntityMapper.class})
public interface CategoryEntityMapper {

    /**
     * Converte uma entidade JPA para uma entidade de domínio
     * @param entity Entidade JPA
     * @param cycleAvoidingMappingContext Contexto para evitar ciclos
     * @return Entidade de domínio
     */
    Category toDomain(CategoryEntity entity, @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    /**
     * Converte uma lista de entidades JPA para uma lista de entidades de domínio
     * @param entities Lista de entidades JPA
     * @param cycleAvoidingMappingContext Contexto para evitar ciclos
     * @return Lista de entidades de domínio
     */
    List<Category> toDomainList(List<CategoryEntity> entities, @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    /**
     * Converte uma entidade de domínio para uma entidade JPA
     * @param domain Entidade de domínio
     * @return Entidade JPA
     */
    CategoryEntity toEntity(Category domain, @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @DoIgnore
    default Category toDomain(CategoryEntity entity) {
        return toDomain(entity, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<Category> toDomainList(List<CategoryEntity> entities) {
        return toDomainList(entities, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default CategoryEntity toEntity(Category domain) {
        return toEntity(domain, new CycleAvoidingMappingContext());
    }
}