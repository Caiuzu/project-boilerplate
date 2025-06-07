package com.app.core.api.catalog.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "CatalogResponse", description = "DTO de resposta para um catálogo")
public class CatalogResponse {

    @Schema(description = "ID do catálogo", example = "1")
    private Long id;

    @Schema(description = "Nome do catálogo", example = "Promoções de Verão")
    private String name;

    @Schema(description = "Data de criação", example = "2024-01-10T14:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Data da última atualização", example = "2024-02-15T09:00:00")
    private LocalDateTime updatedAt;
}