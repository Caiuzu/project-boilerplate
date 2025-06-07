package com.app.core.api.catalog.application.mapper.response;

import com.app.core.api.catalog.application.dto.response.CatalogResponse;
import com.app.core.api.catalog.domain.model.Catalog;
import com.app.core.api.shared.mapper.AuditInfoMapper;
import com.app.core.api.shared.mapper.CycleAvoidingMappingContext;
import com.app.core.api.shared.mapper.DoIgnore;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * Mapper que converte a entidade {@link Catalog} para o DTO {@link CatalogResponse}.
 * Utiliza {@link CategoryResponseMapper} para conversão de categorias associadas.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {CategoryResponseMapper.class, AuditInfoMapper.class})
public interface CatalogResponseMapper {

    /**
     * Converte a entidade {@link Catalog} para {@link CatalogResponse}, utilizando um contexto para evitar ciclos.
     *
     * @param catalog Entidade Catalog.
     * @param cycleAvoidingMappingContext Contexto para evitar ciclos de mapeamento.
     * @return DTO CatalogResponse.
     */
    @Mapping(source = "auditInfo", target = "createdAt", qualifiedByName = "mapCreatedAt")
    @Mapping(source = "auditInfo", target = "updatedAt", qualifiedByName = "mapUpdatedAt")
    CatalogResponse toResponse(Catalog catalog, @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    /**
     * Converte uma lista de entidades {@link Catalog} para uma lista de {@link CatalogResponse}, com contexto.
     *
     * @param catalogs Lista de entidades Catalog.
     * @param cycleAvoidingMappingContext Contexto para evitar ciclos de mapeamento.
     * @return Lista de DTOs CatalogResponse.
     */
    @Mapping(source = "auditInfo", target = "createdAt", qualifiedByName = "mapCreatedAt")
    @Mapping(source = "auditInfo", target = "updatedAt", qualifiedByName = "mapUpdatedAt")
    List<CatalogResponse> toResponseList(List<Catalog> catalogs, @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    /**
     * Converte a entidade {@link Catalog} para {@link CatalogResponse} com um contexto padrão.
     *
     * @param catalog Entidade Catalog.
     * @return DTO CatalogResponse.
     */
    @DoIgnore
    default CatalogResponse toResponse(Catalog catalog) {
        return toResponse(catalog, new CycleAvoidingMappingContext());
    }

    /**
     * Converte uma lista de {@link Catalog} para uma lista de {@link CatalogResponse} com contexto padrão.
     *
     * @param catalogs Lista de entidades Catalog.
     * @return Lista de DTOs CatalogResponse.
     */
    @DoIgnore
    default List<CatalogResponse> toResponseList(List<Catalog> catalogs) {
        return toResponseList(catalogs, new CycleAvoidingMappingContext());
    }
}

