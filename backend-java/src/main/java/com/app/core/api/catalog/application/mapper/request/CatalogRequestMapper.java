package com.app.core.api.catalog.application.mapper.request;

import com.app.core.api.catalog.application.dto.request.CatalogRequest;
import com.app.core.api.catalog.domain.model.Catalog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper que converte o DTO CatalogRequest para a entidade de domínio Catalog
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CatalogRequestMapper {

    /**
     * Converte um DTO de requisição para a entidade de domínio Catalog.
     *
     * @param request DTO de criação do catálogo
     * @return Entidade de domínio Catalog
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "categories", ignore = true)
    Catalog toDomain(CatalogRequest request);
}
