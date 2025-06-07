package com.app.core.api.catalog.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para requisições de criação/atualização de catalogos
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "CatalogRequest", description = "DTO para criação de um catálogo")
public class CatalogRequest {
    
    @NotBlank(message = "O nome do catálogo é obrigatório")
    @Size(max = 100, message = "O nome do catálogo deve ter no máximo 100 caracteres")
    @Schema(description = "Nome do catálogo", example = "Promoções", required = true)
    private String name;
} 