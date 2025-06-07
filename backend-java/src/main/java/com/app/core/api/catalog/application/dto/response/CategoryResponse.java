package com.app.core.api.catalog.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para respostas de categorias
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "CategoryResponse", description = "DTO para resposta com informações de categorias de produtos")
public class CategoryResponse {

    @Schema(description = "ID da categoria", example = "1")
    private Long id;

    @Schema(description = "Nome da categoria", example = "Lanches")
    private String name;

    @Schema(description = "Descrição da categoria", example = "Lanches rápidos e saborosos")
    private String description;

    @Schema(description = "URL da imagem da categoria", example = "images/lanches.png")
    private String imageUrl;

    @Schema(description = "Ordem de exibição no catálogo", example = "2")
    private Integer displayOrder;

    @Schema(description = "Indica se a categoria está ativa", example = "true")
    private boolean active;

    @Schema(description = "Data de criação", example = "2024-01-10T14:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Data da última atualização", example = "2024-02-15T09:00:00")
    private LocalDateTime updatedAt;
} 