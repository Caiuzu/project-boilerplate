package com.app.core.api.catalog.domain.model;

import com.app.core.api.catalog.domain.exceptions.CatalogException;
import com.app.core.api.catalog.domain.exceptions.CategoryConflictException;
import com.app.core.api.catalog.domain.exceptions.CategoryNotFoundException;
import com.app.core.api.catalog.domain.exceptions.ProductNotFoundException;
import com.app.core.api.shared.exception.BusinessException;
import com.app.core.api.shared.vo.AuditInfo;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Entidade de domínio que representa um catalogo de produto
 * AGGREGATE ROOT:
 *  - Toda modificação de entidades internas do agregado devem passar pela entidade raíz;
 *  - Único ponto de entrada para qualquer entidade interna do agregado (Lei de Demeter);
 *  - Entidades dentro deste agregado podem se referenciar via id ou objeto;
 *  - Entidades de outros agregados só podem referenciar esta entidade raiz, e isto deve ser via Id;
 */
@Data
public class Catalog {
    private Long id;
    private String name;
    private AuditInfo auditInfo = new AuditInfo();

    private List<Category> categories = new ArrayList<>();

    /**
     * Construtor que inicializa o catálogo com um nome.
     *
     * @param name o nome do catálogo
     * @throws CatalogException se o nome for inválido
     */
    public Catalog(String name) {
        validate(name);
        this.name = name;
    }

    /**
     * Valida o nome do catálogo.
     *
     * @param name nome a ser validado
     * @throws NullPointerException se o nome for nulo
     * @throws CatalogException se o nome tiver mais que 100 caracteres
     */
    private void validate(String name) {
        Objects.requireNonNull(name, "O nome do catalogo não pode ser nulo");

        if (name.trim().length() > 100) {
            throw new CatalogException("Nome do catalogo deve ter no máximo 100 caracteres");
        }
    }

    /**
     * Fornece uma lista imutável de categorias
     * @return lista imutável de categorias
     */
    public List<Category> getCategories() {
        return Collections.unmodifiableList(this.categories);
    }

    /**
     * Retorna uma categoria pelo ID.
     *
     * @param categoryId o ID da categoria
     * @return a categoria correspondente
     * @throws CategoryNotFoundException se a categoria não for encontrada
     */
    public Category getCategoryById(Long categoryId) {
        Objects.requireNonNull(categoryId, "O ID da categoria não pode ser nulo");

        return categories.stream()
                .filter(o -> o.getId().equals(categoryId))
                .findFirst()
                .orElseThrow(() -> new CategoryNotFoundException("Categoria",  categoryId));
    }

    /**
     * Retorna um produto específico dentro de uma categoria.
     *
     * @param categoryId o ID da categoria
     * @param productId o ID do produto
     * @return o produto correspondente
     * @throws CatalogException se o produto ou categoria não for encontrado
     */
    public Product getProductFromCategoryById(Long categoryId, Long productId) {
        Objects.requireNonNull(categoryId, "O ID da categoria não pode ser nulo");
        Objects.requireNonNull(productId, "O ID do produto não pode ser nulo");

        var category = getCategoryById(categoryId);
        return category.getProductById(productId);
    }

    /**
     * Retorna um produto específico presente em qualquer categoria deste catálogo.
     *
     * @param productId o ID do produto a ser buscado
     * @return o produto correspondente
     * @throws ProductNotFoundException se o produto não for encontrado em nenhuma categoria
     */
    public Product getProductById(Long productId) {
        var product = categories.stream()
                .flatMap(c -> c.getProducts().stream())
                .filter(p -> p.getId().equals(productId))
                .findFirst();

        if (product.isEmpty()) {
            throw new ProductNotFoundException("Produto", productId);
        }

        return product.get();
    }


    /**
     * Retorna todos os produtos dentro de uma categoria.
     *
     * @param categoryId o ID da categoria
     * @return todos oso produtos correspondentes
     * @throws CategoryNotFoundException se a categoria não for encontrada
     */
    public List<Product> getProductsFromCategoryById(Long categoryId) {
        Objects.requireNonNull(categoryId, "O ID da categoria não pode ser nulo");

        var category = getCategoryById(categoryId);

        return category.getProducts();
    }

    /**
     * Retorna o último produto dentro de uma categoria.
     *
     * @param categoryId o ID da categoria
     * @return o produto correspondente
     */
    public Product getLastProductOfCategory(Long categoryId) {
        Objects.requireNonNull(categoryId, "O ID da categoria não pode ser nulo");

        var category = getCategoryById(categoryId);

        return category.getLastProduct();
    }

    /**
     * Atualiza o nome do catálogo.
     *
     * @param name o novo nome
     * @throws CatalogException se o nome tiver mais que 100 caracteres
     */
    public void setName(String name) {
        if (name.trim().length() > 100) {
            throw new CatalogException("Nome do catalogo deve ter no máximo 100 caracteres");
        }

        this.name = name;
    }

    /**
     * Adiciona uma nova categoria ao catálogo.
     *
     * @param category a categoria a ser adicionada
     * @throws CategoryConflictException se já existir categoria com o mesmo nome
     */
    public void addCategory(Category category) {
        Objects.requireNonNull(category, "A categoria não pode ser nula");

        if (categories.stream().anyMatch(c -> c.getName().equals(category.getName()))) {
            throw new CategoryConflictException("Categoria", "Nome", category.getName());
        }

        category.setCatalog(this);
        categories.add(category);
    }

    /**
     * Atualiza uma categoria existente no catálogo.
     *
     * @param newCategory a nova categoria com os dados atualizados
     * @throws CategoryConflictException se já existir categoria com o mesmo nome
     * @throws CategoryNotFoundException se a categoria não for encontrada
     */
    public void updateCategory(Category newCategory) {
        Objects.requireNonNull(newCategory, "A categoria não pode ser nula");

        var currentCategory = getCategoryById(newCategory.getId());

        if (categories.stream().anyMatch(c -> c.getName().equals(newCategory.getName()) && !c.getId().equals(newCategory.getId()))) {
            throw new CategoryConflictException("Categoria", "Nome", newCategory.getName());
        }

        currentCategory.setDetails(newCategory.getDetails());
        currentCategory.setDisplayOrder(newCategory.getDisplayOrder());
        currentCategory.setActive(newCategory.isActive());
        currentCategory.markUpdatedNow();
    }

    /**
     * Remove uma categoria do catálogo.
     *
     * @param categoryId o ID da categoria a ser removida
     * @param invalidateCatalog se true, remove a associação da categoria com o catálogo
     * @throws CategoryConflictException se a categoria tiver produtos associados
     * @throws CategoryNotFoundException se a categoria não for encontrada
     */
    public void removeCategory(Long categoryId, boolean invalidateCatalog) {
        Objects.requireNonNull(categoryId, "O ID da categoria não pode ser nula");

        var category = getCategoryById(categoryId);

        category.setCatalog((invalidateCatalog) ? null : category.getCatalog());

        if (!category.getProducts().isEmpty() && category.getCatalog() == null) {
            throw new CategoryConflictException("Não é possível excluir esta categoria porque existem produtos associados a ela");
        }

        categories.remove(category);
    }

    /**
     * Move uma categoria para um novo catálogo.
     *
     * @param newCatalog o novo catálogo que receberá a categoria
     * @param categoryId o ID da categoria a ser movida
     */
    public void moveCatalogCategory (Catalog newCatalog, Long categoryId) {

        var category = getCategoryById(categoryId);

        newCatalog.addCategory(category);
        category.setCatalog(newCatalog);
        category.markUpdatedNow();
        removeCategory(categoryId, false);
    }


    /**
     * Adiciona um produto a uma categoria específica do catálogo.
     *
     * @param categoryId o ID da categoria
     * @param product o produto a ser adicionado
     */
    public void addProductToCategory(Long categoryId, Product product) {
        Objects.requireNonNull(categoryId, "O ID da categoria não pode ser nulo");
        Objects.requireNonNull(product, "O produto não pode ser nulo");

        var category = getCategoryById(categoryId);
        category.addProduct(product);
    }

    /**
     * Atualiza um produto dentro de uma categoria.
     *
     * @param categoryId o ID da categoria do produto
     * @param newProduct os novos dados do produto
     */
    public void updateProductInCategory(Long categoryId, Product newProduct) {
        Objects.requireNonNull(categoryId, "O ID da categoria não pode ser nulo");
        Objects.requireNonNull(newProduct, "O produto não pode ser nulo");

        var category = getCategoryById(categoryId);

        category.updateProduct(newProduct);
    }

    /**
     * Move um produto para uma nova categoria.
     *
     * @param newCategoryId ID da nova categoria que receberá o produto
     * @param productId o ID do produto a ser movida
     */
    public void moveCategoryProduct(Long currentCategoryId, Long newCategoryId, Long productId) {

        var product = getProductFromCategoryById(currentCategoryId, productId);
        var newCategory = getCategoryById(newCategoryId);

        newCategory.moveCategoryProduct(product);
    }

    /**
     * Remove um produto de uma categoria.
     *
     * @param categoryId o ID da categoria
     * @param productId o ID do produto a ser removido
     */
    public void removeProductFromCategory(Long categoryId, Long productId) {
        Objects.requireNonNull(categoryId, "O ID da categoria não pode ser nulo");
        Objects.requireNonNull(productId, "O ID do produto não pode ser nulo");

        var category = getCategoryById(categoryId);

        category.removeProduct(productId);
    }

    /**
     * Atualiza a quantidade de estoque de um produto.
     *
     * @param productId o ID do produto
     * @param newQuantity a nova quantidade de estoque
     */
    public void updateProductStockQuantity(Long productId, int newQuantity) {
        Objects.requireNonNull(productId, "O ID produto não pode ser nulo");

        var product = getProductById(productId);
        product.setStockQuantity(newQuantity);
    }

    /**
     * Obtem a quantidade de estoque de um produto.
     *
     * @param productId o ID do produto
     */
    public Integer getProductStockQuantity(Long productId) {
        Objects.requireNonNull(productId, "O ID produto não pode ser nulo");

        var product = getProductById(productId);
        return product.getStockQuantity();
    }

    /**
     * Atualiza o campo updatedAt com o horário atual.
     *
     * @throws BusinessException se o horário atual for menor ou igual ao createdAt
     */
    public void markUpdatedNow() {
        this.auditInfo.setUpdatedAt(LocalDateTime.now());
    }
}