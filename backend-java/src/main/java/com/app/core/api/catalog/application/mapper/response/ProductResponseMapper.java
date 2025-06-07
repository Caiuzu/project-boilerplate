package com.app.core.api.catalog.application.mapper.response;

import com.app.core.api.catalog.application.dto.response.ProductResponse;
import com.app.core.api.catalog.application.mapper.shared.DetailsMapper;
import com.app.core.api.catalog.application.mapper.shared.ImageUrlMapper;
import com.app.core.api.catalog.domain.model.Product;
import com.app.core.api.shared.mapper.AuditInfoMapper;
import com.app.core.api.shared.mapper.CycleAvoidingMappingContext;
import com.app.core.api.shared.mapper.DoIgnore;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * Mapper que converte a entidade {@link Product} para o DTO {@link ProductResponse}.
 * Utiliza {@link StockResponseMapper} e {@link ImageUrlMapper} para conversão de estoque e URLs de imagem.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {StockResponseMapper.class, ImageUrlMapper.class, DetailsMapper.class, AuditInfoMapper.class})
public interface ProductResponseMapper {

    /**
     * Converte a entidade {@link Product} para {@link ProductResponse}, mapeando a URL da imagem como string.
     *
     * @param product Entidade Product.
     * @param cycleAvoidingMappingContext Contexto para evitar ciclos de mapeamento.
     * @return DTO ProductResponse.
     */
    @Mapping(source = "imageUrl", target = "imageUrl", qualifiedByName = "mapImageUrlToString")
    @Mapping(source = "details", target = "name", qualifiedByName = "mapDetailsToName")
    @Mapping(source = "details", target = "description", qualifiedByName = "mapDetailsToDescription")
    @Mapping(source = "auditInfo", target = "createdAt", qualifiedByName = "mapCreatedAt")
    @Mapping(source = "auditInfo", target = "updatedAt", qualifiedByName = "mapUpdatedAt")
    ProductResponse toResponse(Product product, @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    /**
     * Converte uma lista de {@link Product} para uma lista de {@link ProductResponse}, com contexto.
     *
     * @param products Lista de entidades Product.
     * @param cycleAvoidingMappingContext Contexto para evitar ciclos de mapeamento.
     * @return Lista de DTOs ProductResponse.
     */
    @Mapping(source = "imageUrl", target = "imageUrl", qualifiedByName = "mapImageUrlToString")
    @Mapping(source = "details", target = "name", qualifiedByName = "mapDetailsToName")
    @Mapping(source = "details", target = "description", qualifiedByName = "mapDetailsToDescription")
    @Mapping(source = "auditInfo", target = "createdAt", qualifiedByName = "mapCreatedAt")
    @Mapping(source = "auditInfo", target = "updatedAt", qualifiedByName = "mapUpdatedAt")
    List<ProductResponse> toResponseList(List<Product> products, @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    /**
     * Converte a entidade {@link Product} para {@link ProductResponse} com um contexto padrão.
     *
     * @param product Entidade Product.
     * @return DTO ProductResponse.
     */
    @DoIgnore
    default ProductResponse toResponse(Product product) {
        return toResponse(product, new CycleAvoidingMappingContext());
    }

    /**
     * Converte uma lista de {@link Product} para uma lista de {@link ProductResponse} com contexto padrão.
     *
     * @param products Lista de entidades Product.
     * @return Lista de DTOs ProductResponse.
     */
    @DoIgnore
    default List<ProductResponse> toResponseList(List<Product> products) {
        return toResponseList(products, new CycleAvoidingMappingContext());
    }
}
