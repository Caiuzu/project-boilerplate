package com.app.core.api.catalog.infrastructure.adapters.in.controller;

import com.app.core.api.catalog.application.dto.request.CatalogRequest;
import com.app.core.api.catalog.application.dto.request.CategoryRequest;
import com.app.core.api.catalog.application.dto.request.ProductRequest;
import com.app.core.api.catalog.application.dto.response.CatalogResponse;
import com.app.core.api.catalog.application.dto.response.CategoryResponse;
import com.app.core.api.catalog.application.dto.response.ProductResponse;
import com.app.core.api.catalog.application.ports.in.CatalogUseCase;
import com.app.core.api.catalog.application.services.CatalogService;
import com.app.core.api.shared.infrastructure.adapters.out.logging.CustomLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/catalogs")
public class CatalogController {

    private final CatalogUseCase catalogUseCase;
    private final CustomLogger logger;

    public CatalogController(CatalogService catalogService) {
        this.catalogUseCase = catalogService;
        this.logger = CustomLogger.getLogger(getClass());
    }

    // ========== CATÁLOGOS ==========
    @GetMapping
    @Operation(
            summary = "Listar todos os catálogos",
            description = "Retorna uma lista com todos os catálogos cadastrados",
            security = @SecurityRequirement(name = "bearer-key"),
            tags = { "Catálogos" }
    )
    @ApiResponse(responseCode = "200", description = "Lista de catálogos retornada com sucesso",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = CatalogResponse.class))))
    @Tag(name = "Catálogos", description = "Operações para gerenciamento de catálogos de categorias de produtos")
    public ResponseEntity<List<CatalogResponse>> getAllCatalogs() {
        logger.debug("Requisição para listar todos os catálogos");
        return ResponseEntity.ok(catalogUseCase.getAllCatalogs());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar catálogo por ID",
            description = "Retorna um catálogo específico pelo seu ID",
            security = @SecurityRequirement(name = "bearer-key"),
            tags = { "Catálogos" }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Catálogo encontrado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CatalogResponse.class))),
            @ApiResponse(responseCode = "404", description = "Catálogo não encontrado", content = @Content)
    })
    @Tag(name = "Catálogos", description = "Operações para gerenciamento de catálogos de categorias de produtos")
    public ResponseEntity<CatalogResponse> getCatalogById(
            @Parameter(description = "ID do catálogo", example = "1", required = true)
            @PathVariable Long id) {
        logger.debug("Requisição para buscar catálogo por ID: {}", id);
        return ResponseEntity.ok(catalogUseCase.getCatalogById(id));
    }

    @PostMapping
    @Operation(
            summary = "Criar novo catálogo",
            description = "Cria um novo catálogo com os dados fornecidos",
            security = @SecurityRequirement(name = "bearer-key"),
            tags = { "Catálogos" }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Catálogo criado com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CatalogResponse.class))),
            @ApiResponse(responseCode = "409", description = "Catálogo com nome já existente", content = @Content),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
    })
    @Tag(name = "Catálogos", description = "Operações para gerenciamento de catálogos de categorias de produtos")
    public ResponseEntity<CatalogResponse> createCatalog(
            @Valid @RequestBody CatalogRequest request) {
        logger.debug("Requisição para criar novo catálogo");
        var response = catalogUseCase.saveCatalog(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Atualizar catálogo",
            description = "Atualiza os dados de um catálogo existente pelo seu ID",
            security = @SecurityRequirement(name = "bearer-key"),
            tags = { "Catálogos" }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Catálogo atualizado com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CatalogResponse.class))),
            @ApiResponse(responseCode = "404", description = "Catálogo não encontrado", content = @Content),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
            @ApiResponse(responseCode = "409", description = "Catálogo com nome já existente", content = @Content),
    })
    @Tag(name = "Catálogos", description = "Operações para gerenciamento de catálogos de categorias de produtos")
    public ResponseEntity<CatalogResponse> updateCatalog(
            @Parameter(description = "ID do catálogo", example = "1", required = true)
            @PathVariable Long id,
            @Valid @RequestBody CatalogRequest request) {
        logger.debug("Requisição para atualizar catálogo: {}", id);
        var response = catalogUseCase.updateCatalog(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Excluir catálogo",
            description = "Exclui um catálogo pelo seu ID",
            security = @SecurityRequirement(name = "bearer-key"),
            tags = { "Catálogos" }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Catálogo excluído com sucesso", content = @Content),
            @ApiResponse(responseCode = "404", description = "Catálogo não encontrado", content = @Content),
            @ApiResponse(responseCode = "409", description = "Catálogo possui categorias associadas", content = @Content)
    })
    @Tag(name = "Catálogos", description = "Operações para gerenciamento de catálogos de categorias de produtos")
    public ResponseEntity<Void> deleteCatalog(
            @Parameter(description = "ID do catálogo", example = "1", required = true)
            @PathVariable Long id) {
        logger.debug("Requisição para excluir catálogo: {}", id);
        catalogUseCase.deleteCatalog(id);
        return ResponseEntity.noContent().build();
    }

    // ========== CATEGORIAS ==========
    @PostMapping("/categories")
    @Operation(
            summary = "Criar nova categoria",
            description = "Cria uma nova categoria vinculada a um catálogo existente",
            security = @SecurityRequirement(name = "bearer-key"),
            tags = { "Categorias" }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Categoria criada com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CategoryResponse.class))),
            @ApiResponse(responseCode = "404", description = "Catálogo não encontrado", content = @Content),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
            @ApiResponse(responseCode = "409", description = "Categoria com nome já existente no catálogo", content = @Content)
    })
    @Tag(name = "Categorias", description = "Operações para gerenciamento de categorias de produtos")
    public ResponseEntity<CategoryResponse> createCategory(
            @Valid @RequestBody CategoryRequest request) {
        logger.debug("Requisição para criar nova categoria no catálogo: {}", request.getCatalogId());
        CategoryResponse response = catalogUseCase.saveCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{catalogId}/categories")
    @Operation(
            summary = "Listar categorias do catálogo",
            description = "Retorna todas as categorias associadas a um catálogo",
            security = @SecurityRequirement(name = "bearer-key"),
            tags = { "Categorias" }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de categorias retornada com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = CategoryResponse.class)))),
            @ApiResponse(responseCode = "404", description = "Catálogo não encontrado", content = @Content)
    })
    @Tag(name = "Categorias", description = "Operações para gerenciamento de categorias de produtos")
    public ResponseEntity<List<CategoryResponse>> getAllCategories(
            @Parameter(description = "ID do catálogo", example = "1", required = true)
            @PathVariable Long catalogId) {
        List<CategoryResponse> responseList = catalogUseCase.getAllCategories(catalogId);
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/{catalogId}/categories/{categoryId}")
    @Operation(
            summary = "Buscar categoria por ID",
            description = "Retorna uma categoria específica de um catálogo pelo ID da categoria",
            security = @SecurityRequirement(name = "bearer-key"),
            tags = { "Categorias" }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoria encontrada com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CategoryResponse.class))),
            @ApiResponse(responseCode = "404", description = "Categoria ou catálogo não encontrado", content = @Content)
    })
    @Tag(name = "Categorias", description = "Operações para gerenciamento de categorias de produtos")
    public ResponseEntity<CategoryResponse> getCategoryById(
            @Parameter(description = "ID do catálogo", example = "1", required = true)
            @PathVariable Long catalogId,
            @Parameter(description = "ID da categoria", example = "10", required = true)
            @PathVariable Long categoryId
    ) {
        CategoryResponse response = catalogUseCase.getCategoryById(catalogId, categoryId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{catalogId}/categories/{categoryId}")
    @Operation(
            summary = "Atualizar categoria",
            description = "Atualiza uma categoria vinculada a um catálogo existente",
            security = @SecurityRequirement(name = "bearer-key"),
            tags = { "Categorias" }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoria atualizada com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CategoryResponse.class))),
            @ApiResponse(responseCode = "404", description = "Catálogo não encontrado", content = @Content),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
            @ApiResponse(responseCode = "409", description = "Categoria com nome já existente no catálogo", content = @Content)
    })
    @Tag(name = "Categorias", description = "Operações para gerenciamento de categorias de produtos")
    public ResponseEntity<CategoryResponse> updateCategory(
            @Parameter(description = "ID atual do catálogo da categoria", example = "2", required = true)
            @PathVariable Long catalogId,
            @Parameter(description = "ID da categoria", example = "1", required = true)
            @PathVariable Long categoryId,
            @Valid @RequestBody CategoryRequest request) {
        logger.debug("Requisição para atualizar categoria no catálogo: {}", request.getCatalogId());
        CategoryResponse response = catalogUseCase.updateCategory(catalogId, categoryId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{catalogId}/categories/{categoryId}")
    @Operation(
            summary = "Excluir categoria",
            description = "Exclui uma categoria específica de um catálogo",
            security = @SecurityRequirement(name = "bearer-key"),
            tags = { "Categorias" }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Categoria excluída com sucesso", content = @Content),
            @ApiResponse(responseCode = "404", description = "Categoria ou catálogo não encontrado", content = @Content),
            @ApiResponse(responseCode = "409", description = "Categoria possui produtos associados", content = @Content)
    })
    @Tag(name = "Categorias", description = "Operações para gerenciamento de categorias de produtos")
    public ResponseEntity<Void> deleteCategory(
            @Parameter(description = "ID do catálogo", example = "1", required = true)
            @PathVariable Long catalogId,
            @Parameter(description = "ID da categoria", example = "10", required = true)
            @PathVariable Long categoryId
    ) {
        catalogUseCase.deleteCategory(catalogId, categoryId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(value = "/{catalogId}/categories/{categoryId}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Atualizar imagem do category",
            description = "Atualiza apenas a imagem de um categoryexistente",
            security = @SecurityRequirement(name = "bearer-key"),
            tags = { "Categorias" }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Imagem da categoria atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Catálogo ou categoria não encontrado", content = @Content),
            @ApiResponse(responseCode = "400", description = "Imagem inválida", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro ao processar imagem", content = @Content)
    })
    @Tag(name = "Categorias", description = "Operações para gerenciamento de categorias de produtos")
    public ResponseEntity<Void> updateCategoryImage(
            @Parameter(description = "ID do catálogo", example = "1", required = true)
            @PathVariable Long catalogId,
            @Parameter(description = "ID da categoria", example = "10", required = true)
                    @PathVariable Long categoryId,
            @Parameter(description = "Arquivo da nova imagem", required = true)
            @RequestPart("imageFile") MultipartFile imageFile
    ) {
        logger.debug("Requisição para atualizar imagem da categoria {} do catálogo {}", categoryId, catalogId);
        catalogUseCase.updateCategoryImage(catalogId, categoryId, imageFile);
        return ResponseEntity.noContent().build();
    }

    // ========== PRODUTOS ==========
    @PostMapping("/{catalogId}/categories/products")
    @Operation(
            summary = "Criar novo produto",
            description = "Cria um novo produto vinculado a uma categoria existente",
            security = @SecurityRequirement(name = "bearer-key"),
            tags = { "Produtos" }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Produto criado com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "404", description = "Catálogo ou categoria não encontrada", content = @Content),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
            @ApiResponse(responseCode = "409", description = "Produto com nome já existente na categoria", content = @Content)
    })
    @Tag(name = "Produtos", description = "Operações para gerenciamento de produtos")
    public ResponseEntity<ProductResponse> createProduct(
            @Parameter(description = "ID do catálogo", example = "1", required = true)
            @PathVariable Long catalogId,
            @Valid @RequestBody ProductRequest request) {
        logger.debug("Requisição para criar novo produto na categoria {} do catálogo {}", request.getCategoryId(), catalogId);
        ProductResponse response = catalogUseCase.saveProduct(catalogId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{catalogId}/categories/{categoryId}/products")
    @Operation(
            summary = "Listar produtos por categoria",
            description = "Retorna todos os produtos de uma determinada categoria dentro de um catálogo",
            security = @SecurityRequirement(name = "bearer-key"),
            tags = { "Produtos" }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de produtos retornada com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = ProductResponse.class)))),
            @ApiResponse(responseCode = "404", description = "Catálogo ou categoria não encontrada", content = @Content)
    })
    @Tag(name = "Produtos", description = "Operações para gerenciamento de produtos")
    public ResponseEntity<List<ProductResponse>> getAllProductsByCategory(
            @Parameter(description = "ID do catálogo", example = "1", required = true)
            @PathVariable Long catalogId,
            @Parameter(description = "ID da categoria", example = "5", required = true)
            @PathVariable Long categoryId
    ) {
        logger.debug("Requisição para listar produtos da categoria {} no catálogo {}", categoryId, catalogId);
        List<ProductResponse> products = catalogUseCase.getAllProducts(catalogId, categoryId);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{catalogId}/categories/{categoryId}/products/{productId}")
    @Operation(
            summary = "Buscar produto por ID",
            description = "Retorna um produto específico de uma categoria pelo ID do produto",
            security = @SecurityRequirement(name = "bearer-key"),
            tags = { "Produtos" }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto encontrado com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "404", description = "Catálogo, categoria ou produto não encontrado", content = @Content)
    })
    @Tag(name = "Produtos", description = "Operações para gerenciamento de produtos")
    public ResponseEntity<ProductResponse> getProductById(
            @Parameter(description = "ID do catálogo", example = "1", required = true)
            @PathVariable Long catalogId,
            @Parameter(description = "ID da categoria", example = "10", required = true)
            @PathVariable Long categoryId,
            @Parameter(description = "ID do produto", example = "100", required = true)
            @PathVariable Long productId) {
        logger.debug("Requisição para buscar produto {} na categoria {} do catálogo {}", productId, categoryId, catalogId);
        ProductResponse response = catalogUseCase.getProductById(catalogId, categoryId, productId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{catalogId}/categories/{categoryId}/products/{productId}")
    @Operation(
            summary = "Atualizar produto",
            description = "Atualiza os dados de um produto existente",
            security = @SecurityRequirement(name = "bearer-key"),
            tags = { "Produtos" }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "404", description = "Catálogo, categoria ou produto não encontrado", content = @Content),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
            @ApiResponse(responseCode = "409", description = "Produto com nome já existente na categoria", content = @Content)
    })
    @Tag(name = "Produtos", description = "Operações para gerenciamento de produtos")
    public ResponseEntity<ProductResponse> updateProduct(
            @Parameter(description = "ID do catálogo", example = "1", required = true)
            @PathVariable Long catalogId,
            @Parameter(description = "ID atual da categoria do produto", example = "10", required = true)
            @PathVariable Long categoryId,
            @Parameter(description = "ID do produto", example = "100", required = true)
            @PathVariable Long productId,
            @Valid @RequestBody ProductRequest request) {
        logger.debug("Requisição para atualizar produto {} na categoria {} do catálogo {}", productId, categoryId, catalogId);
        ProductResponse response = catalogUseCase.updateProduct(catalogId, categoryId, productId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{catalogId}/categories/{categoryId}/products/{productId}")
    @Operation(
            summary = "Excluir produto",
            description = "Exclui um produto específico de uma categoria",
            security = @SecurityRequirement(name = "bearer-key"),
            tags = { "Produtos" }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Produto excluído com sucesso", content = @Content),
            @ApiResponse(responseCode = "404", description = "Catálogo, categoria ou produto não encontrado", content = @Content)
    })
    @Tag(name = "Produtos", description = "Operações para gerenciamento de produtos")
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "ID do catálogo", example = "1", required = true)
            @PathVariable Long catalogId,
            @Parameter(description = "ID da categoria", example = "10", required = true)
            @PathVariable Long categoryId,
            @Parameter(description = "ID do produto", example = "100", required = true)
            @PathVariable Long productId) {
        logger.debug("Requisição para excluir produto {} da categoria {} do catálogo {}", productId, categoryId, catalogId);
        catalogUseCase.deleteProduct(catalogId, categoryId, productId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(value = "/{catalogId}/categories/{categoryId}/products/{productId}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Atualizar imagem do produto",
            description = "Atualiza apenas a imagem de um produto existente",
            security = @SecurityRequirement(name = "bearer-key"),
            tags = { "Produtos" }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Imagem do produto atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Catálogo, categoria ou produto não encontrado", content = @Content),
            @ApiResponse(responseCode = "400", description = "Imagem inválida", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro ao processar imagem", content = @Content)
    })
    @Tag(name = "Produtos", description = "Operações para gerenciamento de produtos")
    public ResponseEntity<Void> updateProductImage(
            @Parameter(description = "ID do catálogo", example = "1", required = true)
            @PathVariable Long catalogId,
            @Parameter(description = "ID da categoria do produto", example = "10", required = true)
            @PathVariable Long categoryId,
            @Parameter(description = "ID do produto", example = "100", required = true)
            @PathVariable Long productId,
            @Parameter(description = "Arquivo da nova imagem", required = true)
            @RequestPart("imageFile") MultipartFile imageFile
    ) {
        logger.debug("Requisição para atualizar imagem do produto {} na categoria {} do catálogo {}", productId, categoryId, catalogId);
        catalogUseCase.updateProductImage(catalogId, categoryId, productId, imageFile);
        return ResponseEntity.noContent().build();
    }

}