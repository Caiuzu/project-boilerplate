package com.app.core.api.catalog.application.services;

import com.app.core.api.catalog.application.dto.request.CatalogRequest;
import com.app.core.api.catalog.application.dto.request.CategoryRequest;
import com.app.core.api.catalog.application.dto.request.ProductRequest;
import com.app.core.api.catalog.application.dto.response.CatalogResponse;
import com.app.core.api.catalog.application.dto.response.CategoryResponse;
import com.app.core.api.catalog.application.dto.response.ProductResponse;
import com.app.core.api.catalog.application.mapper.request.CatalogRequestMapper;
import com.app.core.api.catalog.application.mapper.request.CategoryRequestMapper;
import com.app.core.api.catalog.application.mapper.request.ProductRequestMapper;
import com.app.core.api.catalog.application.mapper.response.CatalogResponseMapper;
import com.app.core.api.catalog.application.mapper.response.CategoryResponseMapper;
import com.app.core.api.catalog.application.mapper.response.ProductResponseMapper;
import com.app.core.api.catalog.application.ports.in.CatalogUseCase;
import com.app.core.api.catalog.domain.events.ProductCreatedEvent;
import com.app.core.api.catalog.domain.exceptions.CatalogConflictException;
import com.app.core.api.catalog.domain.exceptions.CatalogNotFoundException;
import com.app.core.api.catalog.domain.model.Catalog;
import com.app.core.api.catalog.domain.model.Category;
import com.app.core.api.catalog.domain.model.Product;
import com.app.core.api.catalog.domain.ports.out.CatalogRepository;
import com.app.core.api.order.domain.events.OrderItemCanceledEvent;
import com.app.core.api.order.domain.events.OrderItemCreatedEvent;
import com.app.core.api.order.domain.exceptions.OrderItemNotFoundException;
import com.app.core.api.shared.application.ports.out.ImageStoragePort;
import com.app.core.api.shared.infrastructure.adapters.out.logging.CustomLogger;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
* Serviço que implementa o caso de uso de Catálogo.
 */
@Service
public class CatalogService implements CatalogUseCase {

    private final CatalogRepository catalogRepository;

    private final CatalogRequestMapper catalogRequestMapper;
    private final CategoryRequestMapper categoryRequestMapper;
    private final ProductRequestMapper productRequestMapper;

    private final CatalogResponseMapper catalogResponseMapper;
    private final CategoryResponseMapper categoryResponseMapper;
    private final ProductResponseMapper productResponseMapper;

    private final ImageStoragePort imageStoragePort;

    private final ApplicationEventPublisher eventPublisher;
    private final CustomLogger logger;

    public CatalogService(
            CatalogRepository catalogRepository,
            CatalogRequestMapper catalogRequestMapper,
            CatalogResponseMapper catalogResponseMapper,
            CategoryRequestMapper categoryRequestMapper,
            CategoryResponseMapper categoryResponseMapper,
            ProductRequestMapper productRequestMapper,
            ProductResponseMapper productResponseMapper,
            ApplicationEventPublisher eventPublisher,
            ImageStoragePort imageStoragePort
    ) {
        this.catalogRepository = catalogRepository;
        this.catalogRequestMapper = catalogRequestMapper;
        this.catalogResponseMapper = catalogResponseMapper;
        this.categoryRequestMapper = categoryRequestMapper;
        this.categoryResponseMapper = categoryResponseMapper;
        this.productRequestMapper = productRequestMapper;
        this.productResponseMapper = productResponseMapper;
        this.eventPublisher = eventPublisher;
        this.logger = CustomLogger.getLogger(getClass());
        this.imageStoragePort = imageStoragePort;
    }

    /**
     * Salva um catálogo.
     *
     * @param catalogRequest Catálogo a ser salvo
     * @return Catálogo salvo com identificadores atualizados
     */
    @Override
    @Transactional
    public CatalogResponse saveCatalog(CatalogRequest catalogRequest) {

        var catalog = catalogRequestMapper.toDomain(catalogRequest);
        logger.debug("Criando catalogo: {}", catalog.getName());

        if (catalogRepository.existsByName(catalog.getName())) {
            logger.warn("Tentativa de cadastrar catalogo com nome repetido. Nome: {}", catalog.getName());
            throw new CatalogConflictException("Catalogo", "Nome", catalog.getName());
        }

        var savedCatalog = catalogRepository.save(catalog);

        logger.debug("Catalogo criado com sucesso: {}", catalog.getId());

        return catalogResponseMapper.toResponse(savedCatalog);
    }

    /**
     * Atualiza um catálogo.
     *
     * @param id Identificador do catálogo
     * @param catalogRequest Catálogo a ser atualizado
     * @return Catálogo salvo com identificadores atualizados
     */
    @Override
    @Transactional
    public CatalogResponse updateCatalog(Long id, CatalogRequest catalogRequest) {

        var existingCatalog = catalogRepository.findById(id);

        logger.debug("Atualizando catalogo: {}", id);

        if (existingCatalog.isEmpty()) {
            logger.warn("Tentativa de atualizar catalogo inexistente. Id: {}", id);
            throw new CatalogNotFoundException("Catalogo", id);
        }
        else if (catalogRepository.existsByNameAndIdNot(catalogRequest.getName(), id)) {
            logger.warn("Tentativa de cadastrar catalogo com nome repetido. Nome: {}", catalogRequest.getName());
            throw new CatalogConflictException("Catalogo", "Nome", catalogRequest.getName());
        }

        BeanUtils.copyProperties(catalogRequest, existingCatalog.get());

        existingCatalog.get().markUpdatedNow();
        var updatedCatalog = catalogRepository.save(existingCatalog.get());

        logger.debug("Catalogo atualizado com sucesso: {}", id);

        return catalogResponseMapper.toResponse(updatedCatalog);
    }

    /**
     * Busca um catálogo pelo seu ID.

     * @param id Identificador do catálogo
     * @return o catálogo
     */
    @Override
    @Transactional(readOnly = true)
    public CatalogResponse getCatalogById(Long id) {
        logger.debug("Buscando catalogo de id: {}", id);

        var existingCatalog = catalogRepository.findById(id);

        if (existingCatalog.isEmpty()) {
            logger.warn("Catalogo não encontrado. Id: {}", id);
            throw new CatalogNotFoundException("Catalogo", id);
        }

        return catalogResponseMapper.toResponse(existingCatalog.get());
    }

    /**
     * Lista todos os catálogos.
     *
     * @return Lista contendo todos os catálogos
     */
    @Override
    @Transactional(readOnly = true)
    public List<CatalogResponse> getAllCatalogs() {
        logger.debug("Buscando todos os catálogos");
        var existingCatalogs = catalogRepository.findAll();
        logger.debug("Encontradas {} catalogos", existingCatalogs.size());

        return catalogResponseMapper.toResponseList(existingCatalogs);
    }

    /**
     * Remove um catálogo pelo seu ID.
     *
     * @param id Identificador do catálogo a ser removido
     */
    @Override
    @Transactional
    public void deleteCatalog(Long id) {
        logger.debug("Excluindo catalogo de id: {}", id);

        if (!catalogRepository.existsById(id)) {
            logger.warn("Tentativa de excluir catalogo inexistente. Id: {}", id);
            throw new CatalogNotFoundException("Catalogo", id);
        }
        if (catalogRepository.existsCategoryByCatalogId(id)) {
            logger.warn("Tentativa de excluir catalogo com categorias associadas. Id: {}", id);
            throw new CatalogConflictException("Não é possível excluir este catalogo porque existem categorias associadas a ele");
        }

        catalogRepository.delete(id);

        logger.debug("Catalogo excluido com sucesso: {}", id);
    }

    /**
     * Salva uma categoria.
     *
     * @param categoryRequest Categoria a ser salva
     * @return Categoria salva com identificadores atualizados
     */
    @Override
    @Transactional
    public CategoryResponse saveCategory(CategoryRequest categoryRequest) {

        var category = categoryRequestMapper.toDomain(categoryRequest);
        var catalog = catalogRepository.findById(categoryRequest.getCatalogId());

        logger.debug("Criando categoria: {}", categoryRequest.getName());

        if (catalog.isEmpty()) {
            logger.warn("Tentativa de cadastrar categoria com catalogo inexistente. ID catalogo: {}", categoryRequest.getCatalogId());
            throw new CatalogNotFoundException("Catalogo", categoryRequest.getCatalogId());
        }

        catalog.get().addCategory(category);

        var savedCatalog = catalogRepository.save(catalog.get());
        var savedCategory = savedCatalog.getCategories().getLast();

        var savedCategoryToResponse = categoryResponseMapper.toResponse(savedCategory);

        logger.debug("Categoria criada com sucesso: {}", savedCategoryToResponse.getId());

        return savedCategoryToResponse;
    }

    /**
     * Atualiza uma categoria.
     *
     * @param categoryRequest Categoria a ser atualizada
     * @return Categoria atualizada com identificadores atualizados
     */
    @Override
    @Transactional
    public CategoryResponse updateCategory(Long catalogId, Long categoryId, CategoryRequest categoryRequest) {

        var category = categoryRequestMapper.toDomain(categoryRequest);
        var existingCatalog = catalogRepository.findById(catalogId);
        category.setId(categoryId);

        logger.debug("Atualizando categoria: {}", categoryId);

        if (existingCatalog.isEmpty()) {
            logger.warn("Tentativa de atualizar categoria com catálogo inexistente. Id: {}", catalogId);
            throw new CatalogNotFoundException("Catalogo", catalogId);
        }
        else if (!categoryRequest.getCatalogId().equals(catalogId)) {

            var newCatalog = catalogRepository.findById(categoryRequest.getCatalogId());

            if (newCatalog.isEmpty()) {
                logger.warn("Tentativa de mover categoria para catálogo inexistente. Id: {}", categoryRequest.getCatalogId());
                throw new CatalogNotFoundException("Catalogo", categoryRequest.getCatalogId());
            }

            existingCatalog.get().moveCatalogCategory(newCatalog.get(), categoryId);
            catalogRepository.save(existingCatalog.get());
            logger.debug("Categoria movida com sucesso para catálogo: {}", categoryRequest.getCatalogId());

            newCatalog.get().updateCategory(category);
            var updatedCatalog = catalogRepository.save(newCatalog.get());
            var updatedCategory = updatedCatalog.getCategoryById(categoryId);

            var updatedCategoryToResponse = categoryResponseMapper.toResponse(updatedCategory);

            logger.debug("Categoria atualizada com sucesso: {}", catalogId);

            return updatedCategoryToResponse;
        }

        existingCatalog.get().updateCategory(category);

        var updatedCatalog = catalogRepository.save(existingCatalog.get());
        var updatedCategory = updatedCatalog.getCategoryById(categoryId);

        var updatedCategoryToResponse = categoryResponseMapper.toResponse(updatedCategory);

        logger.debug("Categoria atualizada com sucesso: {}", catalogId);

        return updatedCategoryToResponse;
    }

    /**
     * Busca uma categoria por ID dentro de um catálogo.
     *
     * @param catalogId  ID do catálogo
     * @param categoryId ID da categoria
     * @return Categoria encontrada
     */
    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(Long catalogId, Long categoryId) {
        logger.debug("Buscando categoria de id: {} no catalogo de id: {}", categoryId, catalogId);

        var catalog = catalogRepository.findById(catalogId);

        if (catalog.isEmpty()) {
            logger.warn("Catalogo não encontrado. Id: {}", catalogId);
            throw new CatalogNotFoundException("Catalogo", catalogId);
        }

        var category = catalog.get().getCategoryById(categoryId);

        return categoryResponseMapper.toResponse(category);
    }

    /**
     * Lista todas as categorias de um catálogo.
     *
     * @param catalogId ID do catálogo
     * @return Lista de categorias
     */
    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories(Long catalogId) {
        logger.debug("Buscando todas as categorias do catalogo de id: {}", catalogId);

        var catalog = catalogRepository.findById(catalogId);

        if (catalog.isEmpty()) {
            logger.warn("Catalogo não encontrado. Id: {}", catalogId);
            throw new CatalogNotFoundException("Catalogo", catalogId);
        }

        var categories = catalog.get().getCategories();

        logger.debug("Encontradas {} categorias", categories.size());

        return categoryResponseMapper.toResponseList(categories);
    }

    /**
     * Exclui uma categoria de um catálogo.
     *
     * @param catalogId  ID do catálogo
     * @param categoryId ID da categoria
     */
    @Override
    @Transactional
    public void deleteCategory(Long catalogId, Long categoryId) {
        logger.debug("Excluindo categoria de id: {} do catalogo de id: {}", categoryId, catalogId);

        var catalog = catalogRepository.findById(catalogId);

        if (catalog.isEmpty()) {
            logger.warn("Tentativa de excluir categoria com catálogo inexistente. Id: {}", catalogId);
            throw new CatalogNotFoundException("Catalogo", catalogId);
        }


        catalog.get().removeCategory(categoryId, true);

        catalogRepository.save(catalog.get());

        logger.debug("Categoria excluída com sucesso: {}", categoryId);
    }




    /**
     * Atualiza apenas a imagem de uma categoria existente.
     *
     * @param catalogId ID do catálogo
     * @param categoryId ID da categoria do categoria
     * @param imageFile Arquivo da nova imagem
     * @throws CatalogNotFoundException se o catálogo não for encontrado
     * @throws IllegalArgumentException se o arquivo de imagem for nulo ou vazio
     * @throws RuntimeException se ocorrer um erro durante o upload da imagem
     */
    @Override
    @Transactional
    public void updateCategoryImage(Long catalogId, Long categoryId, MultipartFile imageFile) {
        logger.debug("Atualizando imagem do categoria ID: {}", categoryId);

        var catalog = catalogRepository.findById(catalogId);

        if (catalog.isEmpty()) {
            logger.warn("Tentativa de excluir categoria com catálogo inexistente. Id: {}", catalogId);
            throw new CatalogNotFoundException("Catalogo", catalogId);
        }

        var newCategory = uploadCategoryImage(catalog.get(), categoryId, imageFile);
        catalog.get().updateCategory(newCategory);
        catalogRepository.save(catalog.get());
    }

    /**
     * Faz upload de uma nova imagem para uma categoria, removendo a anterior se existir.
     *
     * @param catalog Catálogo que contém o categoria
     * @param categoryId ID da categoria do categoria
     * @param imageFile Arquivo da imagem
     * @throws IllegalArgumentException se o arquivo de imagem for nulo ou estiver vazio
     * @throws RuntimeException se ocorrer falha no upload da imagem
     * @return categoria atualizado com a nova URL da imagem
     */
    private Category uploadCategoryImage(Catalog catalog, Long categoryId, MultipartFile imageFile) {

        if (imageFile == null || imageFile.isEmpty()) {
            logger.warn("Tentativa de upload de imagem com arquivo vazio ou nulo");
            throw new IllegalArgumentException("O arquivo de imagem não pode ser vazio");
        }

        var category = catalog.getCategoryById(categoryId);

        try {

            logger.debug("Processando upload de imagem para categoria ID: {}", categoryId);

            if (category.getImageUrl() != null && !category.imageUrlIsEmpty()) {
                var currentImagePath = category.getImageUrlValue();
                logger.debug("Removendo imagem anterior: {}", currentImagePath);
                imageStoragePort.deleteImage(currentImagePath);
            }

            String storagePath = "categories/" + categoryId;
            String imagePath = imageStoragePort.uploadImage(storagePath, imageFile);
            logger.debug("Nova imagem enviada para o caminho: {}", imagePath);

            category.setImageUrlValue(imagePath);

            return category;

        } catch (Exception e) {
            logger.error("Erro ao processar upload de imagem: {}", e.getMessage(), e);
            throw new RuntimeException("Falha ao processar imagem: " + e.getMessage(), e);
        }
    }


    /**
     * Salva um produto.
     *
     * @param catalogId  ID do catálogo ao qual a categoria pertence
     * @param productRequest Produto a ser salvo
     * @return Produto salvo com identificadores atualizados
     */
    @Override
    @Transactional
    public ProductResponse saveProduct(Long catalogId, ProductRequest productRequest) {
        logger.debug("Criando produto: {}", productRequest.getName());

        var catalog = catalogRepository.findById(catalogId);
        var product = productRequestMapper.toDomain(productRequest);

        if (catalog.isEmpty()) {
            logger.warn("Tentativa de cadastrar produto em catálogo inexistente. Id: {}", catalogId);
            throw new CatalogNotFoundException("Catalogo", catalogId);
        }

        catalog.get().addProductToCategory(productRequest.getCategoryId(), product);

        var savedCatalog = catalogRepository.save(catalog.get());
        var savedProduct = savedCatalog.getLastProductOfCategory(productRequest.getCategoryId());

        var productResponse = productResponseMapper.toResponse(savedProduct);

        ProductCreatedEvent event = ProductCreatedEvent.of(
                savedProduct.getId(),
                savedProduct.getName(),
                savedProduct.getPrice(),
                savedProduct.getCategoryId()
        );
        eventPublisher.publishEvent(event);

        logger.debug("Produto criado com sucesso: {}", productResponse.getId());

        return productResponse;
    }

    /**
     * Atualiza um produto.
     *
     * @param catalogId  ID do catálogo
     * @param categoryId ID da categoria
     * @param productId  ID do produto a ser atualizado
     * @param productRequest Produto com os dados atualizados
     * @return Produto atualizado com identificadores atualizados
     */
    @Override
    @Transactional
    public ProductResponse updateProduct(Long catalogId, Long categoryId, Long productId, ProductRequest productRequest) {
        logger.debug("Atualizando produto: {}", productId);

        var catalog = catalogRepository.findById(catalogId);
        var product = productRequestMapper.toDomain(productRequest);
        product.setId(productId);

        if (catalog.isEmpty()) {
            logger.warn("Tentativa de atualizar produto com catálogo inexistente. Id: {}", catalogId);
            throw new CatalogNotFoundException("Catalogo", catalogId);
        }
        else if (!productRequest.getCategoryId().equals(categoryId)) {

            catalog.get().moveCategoryProduct(categoryId, productRequest.getCategoryId(), productId);

            logger.debug("Produto movido com sucesso para categoria: {}", productRequest.getCategoryId());
        }

        catalog.get().updateProductInCategory(productRequest.getCategoryId(), product);

        var updatedCatalog = catalogRepository.save(catalog.get());
        var updatedProduct = updatedCatalog.getProductFromCategoryById(productRequest.getCategoryId(), productId);

        var productResponse = productResponseMapper.toResponse(updatedProduct);

        logger.debug("Produto atualizado com sucesso: {}", productId);

        return productResponse;
    }

    /**
     * Busca um produto por ID dentro de uma categoria.
     *
     * @param catalogId  ID do catálogo
     * @param categoryId ID da categoria
     * @param productId  ID do produto
     * @return Produto encontrado
     */
    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long catalogId, Long categoryId, Long productId) {
        logger.debug("Buscando produto de id: {} na categoria de id: {} no catalogo de id: {}", productId, categoryId, catalogId);

        var catalog = catalogRepository.findById(catalogId);

        if (catalog.isEmpty()) {
            logger.warn("Catalogo não encontrado. Id: {}", catalogId);
            throw new CatalogNotFoundException("Catalogo", catalogId);
        }

        var product = catalog.get().getProductFromCategoryById(categoryId, productId);

        return productResponseMapper.toResponse(product);
    }

    /**
     * Lista todos os produtos de uma categoria.
     *
     * @param catalogId ID do catálogo
     * @return Lista de produtos
     */
    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts(Long catalogId, Long categoryId) {
        logger.debug("Buscando todos os produtos da categoria de id: {}", categoryId);

        var catalog = catalogRepository.findById(catalogId);

        if (catalog.isEmpty()) {
            logger.warn("Catalogo não encontrado. Id: {}", catalogId);
            throw new CatalogNotFoundException("Catalogo", catalogId);
        }

        var products = catalog.get().getProductsFromCategoryById(categoryId);

        logger.debug("Encontrados {} produtos", products.size());

        return productResponseMapper.toResponseList(products);
    }

    /**
     * Exclui um produto de uma categoria.
     *
     * @param catalogId  ID do catálogo
     * @param categoryId ID da categoria
     * @param productId  ID do produto a ser removido
     */
    @Override
    @Transactional
    public void deleteProduct(Long catalogId, Long categoryId, Long productId) {
        logger.debug("Excluindo produto de id: {} da categoria de id: {} do catalogo de id: {}", productId, categoryId, catalogId);

        var catalog = catalogRepository.findById(catalogId);

        if (catalog.isEmpty()) {
            logger.warn("Tentativa de excluir produto com catálogo inexistente. Id: {}", catalogId);
            throw new CatalogNotFoundException("Catalogo", catalogId);
        }

        catalog.get().removeProductFromCategory(categoryId, productId);

        catalogRepository.save(catalog.get());

        logger.debug("Produto excluído com sucesso: {}", productId);
    }

    /**
     * Atualiza apenas a imagem de um produto existente.
     *
     * @param catalogId ID do catálogo
     * @param categoryId ID da categoria do produto
     * @param productId ID do produto
     * @param imageFile Arquivo da nova imagem
     * @throws CatalogNotFoundException se o catálogo não for encontrado
     * @throws IllegalArgumentException se o arquivo de imagem for nulo ou vazio
     * @throws RuntimeException se ocorrer um erro durante o upload da imagem
     */
    @Override
    @Transactional
    public void updateProductImage(Long catalogId, Long categoryId, Long productId, MultipartFile imageFile) {
        logger.debug("Atualizando imagem do produto ID: {}", productId);

        var catalog = catalogRepository.findById(catalogId);

        if (catalog.isEmpty()) {
            logger.warn("Tentativa de excluir produto com catálogo inexistente. Id: {}", catalogId);
            throw new CatalogNotFoundException("Catalogo", catalogId);
        }

        var newProduct = uploadProductImage(catalog.get(), categoryId, productId, imageFile);
        catalog.get().updateProductInCategory(categoryId, newProduct);
        catalogRepository.save(catalog.get());
    }

    /**
     * Faz upload de uma nova imagem para o produto, removendo a anterior se existir.
     *
     * @param catalog Catálogo que contém o produto
     * @param categoryId ID da categoria do produto
     * @param productId ID do produto
     * @param imageFile Arquivo da imagem
     * @throws IllegalArgumentException se o arquivo de imagem for nulo ou estiver vazio
     * @throws RuntimeException se ocorrer falha no upload da imagem
     * @return Produto atualizado com a nova URL da imagem
     */
    private Product uploadProductImage(Catalog catalog, Long categoryId, Long productId, MultipartFile imageFile) {

        if (imageFile == null || imageFile.isEmpty()) {
            logger.warn("Tentativa de upload de imagem com arquivo vazio ou nulo");
            throw new IllegalArgumentException("O arquivo de imagem não pode ser vazio");
        }

        var product = catalog.getProductFromCategoryById(categoryId, productId);

        try {

            logger.debug("Processando upload de imagem para produto ID: {}", productId);

            if (product.getImageUrl() != null && !product.imageUrlIsEmpty()) {
                String currentImagePath = product.getImageUrlValue();
                logger.debug("Removendo imagem anterior: {}", currentImagePath);
                imageStoragePort.deleteImage(currentImagePath);
            }

            String storagePath = "products/" + productId;
            String imagePath = imageStoragePort.uploadImage(storagePath, imageFile);
            logger.debug("Nova imagem enviada para o caminho: {}", imagePath);

            product.setImageUrlValue(imagePath);

            return product;

        } catch (Exception e) {
            logger.error("Erro ao processar upload de imagem: {}", e.getMessage(), e);
            throw new RuntimeException("Falha ao processar imagem: " + e.getMessage(), e);
        }
    }

    /**
     * Atualiza quantidade em estoque de produtos de acordo com a quantidade solicitada em um pedido.
     *
     * @param orderItemCreatedEvents eventos de criação de item de pedido
     */
    @Override
    @Transactional
    public void updateStockForCreatedItems(List<OrderItemCreatedEvent> orderItemCreatedEvents) {
        if (orderItemCreatedEvents.isEmpty()) {
            throw new OrderItemNotFoundException("Lista de itens de pedido está vazia. Não é possível recuperar produtos para atualização de quantidade em estoque.");
        }

        for (OrderItemCreatedEvent orderItemCreatedEvent : orderItemCreatedEvents) {
            var catalog = catalogRepository.findByProductId(orderItemCreatedEvent.getProductId());
            if (catalog.isEmpty()) {
                throw new CatalogNotFoundException("Catálogo do produto do item de pedido não encontrado. Não é possível atualizar quantidade em estoque.");
            }

            var currentProductQuantity = catalog.get().getProductStockQuantity(orderItemCreatedEvent.getProductId());
            var newProductQuantity =  currentProductQuantity - orderItemCreatedEvent.getQuantity();

            logger.info("Iniciando atualização de quantidade em estoque: ProductId {}, atual: {}, nova: {}", orderItemCreatedEvent.getProductId(), currentProductQuantity, newProductQuantity);

            catalog.get().updateProductStockQuantity(orderItemCreatedEvent.getProductId(), newProductQuantity);

            catalogRepository.save(catalog.get());

            logger.info("Atualização de quantidade em estoque realizada com sucesso! ProductId {}", orderItemCreatedEvent.getProductId());
        }

    }

    /**
     * Atualiza quantidade em estoque de produtos de acordo com a quantidade solicitada em um pedido.
     *
     * @param orderItemCanceledEvents eventos de cancelamento de item de pedido
     */
    @Override
    @Transactional
    public void updateStockForCanceledItems(List<OrderItemCanceledEvent> orderItemCanceledEvents) {
        if (orderItemCanceledEvents.isEmpty()) {
            throw new OrderItemNotFoundException("Lista de itens de pedido está vazia. Não é possível recuperar produtos para atualização de quantidade em estoque.");
        }

        for (OrderItemCanceledEvent orderItemCanceledEvent : orderItemCanceledEvents) {
            var catalog = catalogRepository.findByProductId(orderItemCanceledEvent.getProductId());
            if (catalog.isEmpty()) {
                throw new CatalogNotFoundException("Catálogo do produto do item de pedido não encontrado. Não é possível atualizar quantidade em estoque.");
            }

            var currentProductQuantity = catalog.get().getProductStockQuantity(orderItemCanceledEvent.getProductId());
            var newProductQuantity =  currentProductQuantity + orderItemCanceledEvent.getQuantity();

            logger.info("Iniciando atualização de quantidade em estoque: ProductId {}, atual: {}, nova: {}", orderItemCanceledEvent.getProductId(), currentProductQuantity, newProductQuantity);

            catalog.get().updateProductStockQuantity(orderItemCanceledEvent.getProductId(), newProductQuantity);

            catalogRepository.save(catalog.get());

            logger.info("Atualização de quantidade em estoque realizada com sucesso! ProductId {}", orderItemCanceledEvent.getProductId());
        }

    }

}