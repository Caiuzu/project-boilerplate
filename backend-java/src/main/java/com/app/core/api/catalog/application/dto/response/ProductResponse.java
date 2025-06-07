package com.app.core.api.catalog.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para respostas de produtos
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ProductResponse", description = "DTO para resposta com informações de produtos")
public class ProductResponse {

    @Schema(description = "ID do produto", example = "101")
    private Long id;

    @Schema(description = "Nome do produto", example = "X-Burguer")
    private String name;

    @Schema(description = "Descrição do produto", example = "Hamburguer com queijo, alface e tomate")
    private String description;

    @Schema(description = "Preço do produto", example = "19.90")
    private BigDecimal price;

    @Schema(description = "URL da imagem do produto", example = "images/x-burguer.png")
    private String imageUrl;

    @Schema(description = "Indica se o produto está ativo", example = "true")
    private boolean active;

    @Schema(description = "Ordem de exibição no catálogo", example = "1")
    private Integer displayOrder;

    @Schema(description = "Informações do estoque do produto")
    private StockResponse stock;

    @Schema(description = "Data de criação", example = "2024-01-01T10:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "Data da última atualização", example = "2024-02-01T12:00:00")
    private LocalDateTime updatedAt;
} 