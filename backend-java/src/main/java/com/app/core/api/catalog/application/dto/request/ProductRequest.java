package com.app.core.api.catalog.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO para requisições de criação/atualização de produtos
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ProductRequest", description = "DTO para criação ou atualização de produtos")
public class ProductRequest {

    @NotNull(message = "A categoria do produto é obrigatória")
    @Schema(description = "ID da categoria do produto", example = "1", required = true)
    private Long categoryId;

    @NotBlank(message = "O nome do produto é obrigatório")
    @Schema(description = "Nome do produto", example = "X-Burger", required = true)
    private String name;

    @NotBlank(message = "A descrição do produto é obrigatório")
    @Schema(description = "Descrição do produto", example = "Hambúrguer com queijo, alface e tomate", required = true)
    private String description;
    
    @NotNull(message = "O preço do produto é obrigatório")
    @Positive(message = "O preço deve ser maior que zero")
    @Schema(description = "Preço do produto", example = "25.90", required = true)
    private BigDecimal price;

    @NotNull(message = "Quantidade de itens em estoque")
    @Schema(description = "Quantidade de itens em estoque do produto", example = "5", required = true)
    private Integer stockQuantity;
    
    @Schema(description = "Ordem de exibição do produto", example = "1")
    private Integer displayOrder;
} 