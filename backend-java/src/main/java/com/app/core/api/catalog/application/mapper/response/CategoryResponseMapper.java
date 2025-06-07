package com.app.core.api.catalog.application.mapper.response;

import com.app.core.api.catalog.application.dto.response.CategoryResponse;
import com.app.core.api.catalog.application.mapper.shared.DetailsMapper;
import com.app.core.api.catalog.application.mapper.shared.ImageUrlMapper;
import com.app.core.api.catalog.domain.model.Category;
import com.app.core.api.shared.mapper.AuditInfoMapper;
import com.app.core.api.shared.mapper.CycleAvoidingMappingContext;
import com.app.core.api.shared.mapper.DoIgnore;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * Mapper que converte a entidade {@link Category} para o DTO {@link CategoryResponse}.
 * Utiliza {@link ProductResponseMapper} e {@link ImageUrlMapper} para conversão de produtos e URLs de imagem.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {ProductResponseMapper.class, ImageUrlMapper.class, DetailsMapper.class, AuditInfoMapper.class})
public interface CategoryResponseMapper {

    /**
     * Converte a entidade {@link Category} para {@link CategoryResponse}, mapeando a URL da imagem como string.
     *
     * @param category Entidade Category.
     * @param cycleAvoidingMappingContext Contexto para evitar ciclos de mapeamento.
     * @return DTO CategoryResponse.
     */
    @Mapping(source = "imageUrl", target = "imageUrl", qualifiedByName = "mapImageUrlToString")
    @Mapping(source = "details", target = "name", qualifiedByName = "mapDetailsToName")
    @Mapping(source = "details", target = "description", qualifiedByName = "mapDetailsToDescription")
    @Mapping(source = "auditInfo", target = "createdAt", qualifiedByName = "mapCreatedAt")
    @Mapping(source = "auditInfo", target = "updatedAt", qualifiedByName = "mapUpdatedAt")
    CategoryResponse toResponse(Category category, @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    /**
     * Converte uma lista de entidades {@link Category} para uma lista de {@link CategoryResponse}, com contexto.
     *
     * @param categories Lista de entidades Category.
     * @param cycleAvoidingMappingContext Contexto para evitar ciclos de mapeamento.
     * @return Lista de DTOs CategoryResponse.
     */
    @Mapping(source = "imageUrl", target = "imageUrl", qualifiedByName = "mapImageUrlToString")
    @Mapping(source = "details", target = "name", qualifiedByName = "mapDetailsToName")
    @Mapping(source = "details", target = "description", qualifiedByName = "mapDetailsToDescription")
    @Mapping(source = "auditInfo", target = "createdAt", qualifiedByName = "mapCreatedAt")
    @Mapping(source = "auditInfo", target = "updatedAt", qualifiedByName = "mapUpdatedAt")
    List<CategoryResponse> toResponseList(List<Category> categories, @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    /**
     * Converte a entidade {@link Category} para {@link CategoryResponse} com um contexto padrão.
     *
     * @param category Entidade Category.
     * @return DTO CategoryResponse.
     */
    @DoIgnore
    default CategoryResponse toResponse(Category category) {
        return toResponse(category, new CycleAvoidingMappingContext());
    }

    /**
     * Converte uma lista de {@link Category} para uma lista de {@link CategoryResponse} com contexto padrão.
     *
     * @param categories Lista de entidades Category.
     * @return Lista de DTOs CategoryResponse.
     */
    @DoIgnore
    default List<CategoryResponse> toResponseList(List<Category> categories) {
        return toResponseList(categories, new CycleAvoidingMappingContext());
    }
}
