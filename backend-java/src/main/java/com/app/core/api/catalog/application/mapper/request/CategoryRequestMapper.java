package com.app.core.api.catalog.application.mapper.request;

import com.app.core.api.catalog.application.dto.request.CategoryRequest;
import com.app.core.api.catalog.domain.model.Category;
import com.app.core.api.catalog.domain.vo.Details;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper que converte o DTO CategoryRequest para a entidade de domínio Category
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryRequestMapper {

    /**
     * Converte um DTO de requisição para a entidade de domínio Category.
     *
     * @param request DTO de criação da categoria
     * @return Entidade de domínio Category
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "products", ignore = true)
    @Mapping(target = "catalog", ignore = true)
    @Mapping(target = "auditInfo", ignore = true)
    @Mapping(target = "details", source = "request", qualifiedByName = "mapToDetails")
    Category toDomain(CategoryRequest request);

    @Named("mapToDetails")
    default Details mapToDetails(CategoryRequest request) {
        return new Details(request.getName(), request.getDescription());
    }
}