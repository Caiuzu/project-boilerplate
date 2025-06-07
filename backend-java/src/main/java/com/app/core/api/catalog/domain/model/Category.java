package com.app.core.api.catalog.domain.model;

import com.app.core.api.catalog.domain.exceptions.CatalogException;
import com.app.core.api.catalog.domain.exceptions.CategoryException;
import com.app.core.api.catalog.domain.exceptions.ProductConflictException;
import com.app.core.api.catalog.domain.exceptions.ProductNotFoundException;
import com.app.core.api.catalog.domain.exceptions.*;
import com.app.core.api.catalog.domain.vo.Details;
import com.app.core.api.catalog.domain.vo.ImageUrl;
import com.app.core.api.shared.exception.BusinessException;
import com.app.core.api.shared.vo.AuditInfo;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Entidade de domínio que representa uma categoria de produto.
 */
@Getter
@Setter
public class Category {
    private Long id;
    private Details details;
    private ImageUrl imageUrl;
    private Integer displayOrder;
    private boolean active = true;
    private AuditInfo auditInfo = new AuditInfo();

    private Catalog catalog;
    private List<Product> products = new ArrayList<>();

    /**
     * Construtor da categoria de produto.
     *
     * @param details      Detalhes da categoria (nome e descrição)
     * @param imageUrl     URL da imagem associada à categoria
     * @param displayOrder Ordem de exibição da categoria no catálogo
     * @param active       Indica se a categoria está ativa
     * @throws NullPointerException se {@code details} for nulo
     * @throws CatalogException     se {@code displayOrder} for menor que 0
     */
    public Category(
            Details details,
            ImageUrl imageUrl,
            Integer displayOrder,
            boolean active
    ) {
        validate(details, displayOrder);
        this.details = details;
        this.imageUrl = imageUrl;
        this.displayOrder = displayOrder;
        this.active = active;
    }

    /**
     * Valida os atributos obrigatórios da categoria.
     *
     * @param details      Detalhes da categoria
     * @param displayOrder Ordem de exibição
     * @throws NullPointerException se {@code details} for nulo
     * @throws CatalogException     se {@code displayOrder} for menor que 0
     */
    private void validate(Details details, Integer displayOrder) {
        Objects.requireNonNull(details, "Os detalhes da categoria não podem ser nulos");

        if (displayOrder != null && displayOrder <= 0) {
            throw new CatalogException("A ordem de exibição da categoria deve ser maior que 0");
        }
    }

    /**
     * Retorna a URL da imagem do categoria.
     *
     * @return URL da imagem
     */
    public String getImageUrlValue() {
        return this.imageUrl.imageUrl();
    }

    /**
     * Define a url da imagem do categoria.
     *
     */
    public void setImageUrlValue(String imagePath) {
        this.setImageUrl(new ImageUrl(imagePath));
        this.markUpdatedNow();
    }

    /**
     * Verifica se a URL da imagem do categoria está vazia.
     *
     * @return {@code true} se a URL estiver vazia; caso contrário, {@code false}
     */
    public Boolean imageUrlIsEmpty() {
        return this.imageUrl.imageUrl().isEmpty();
    }

    /**
     * Define a ordem de exibição da categoria.
     *
     * @param displayOrder nova ordem de exibição
     * @throws CategoryException se {@code displayOrder} for menor ou igal a zero
     */
    void setDisplayOrder(Integer displayOrder) {
        if (displayOrder != null && displayOrder <= 0) {
            throw new CategoryException("A ordem de exibição da categoria deve ser maior que 0");
        }

        this.displayOrder = displayOrder;
    }

    /**
     * Fornece uma lista imutável de produtos
     * @return lista imutável de produtos
     */
    public List<Product> getProducts() {
        return Collections.unmodifiableList(this.products);
    }

    /**
     * Retorna o último produto da lista de produtos da categoria.
     *
     * @return o último produto da lista
     * @throws ProductNotFoundException se a lista de produtos estiver vazia
     */
    Product getLastProduct() {
        if (products == null || products.isEmpty()) {
            throw new ProductNotFoundException("Nenhum produto encontrado na categoria");
        }
        return products.getLast();
    }

    /**
     * Retorna o nome da categoria.
     *
     * @return nome da categoria
     */
    String getName() {
        return this.details.name();
    }

    /**
     * Retorna a descrição da categoria.
     *
     * @return descrição da categoria
     */
    String getDescription() {
        return this.details.description();
    }

    /**
     * Retorna um produto da categoria com base no ID.
     *
     * @param productId ID do produto
     * @return produto correspondente
     * @throws NullPointerException se {@code productId} for nulo
     * @throws ProductNotFoundException se o produto não for encontrado
     */
    Product getProductById(Long productId) {
        Objects.requireNonNull(productId, "O ID do produto não pode ser nulo");

        return products.stream()
                .filter(p -> p.getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ProductNotFoundException("Produto", productId));
    }

    /**
     * Adiciona um produto à categoria.
     *
     * @param product produto a ser adicionado
     * @throws NullPointerException se {@code product} for nulo
     * @throws ProductConflictException se já existir um produto com o mesmo nome
     */
    void addProduct(Product product) {
        Objects.requireNonNull(product, "O produto não pode ser nulo");

        if (products.stream().anyMatch(p -> p.getName().equals(product.getName()))) {
            throw new ProductConflictException("Produto", "Nome", product.getName());
        }

        product.setActive(product.isActive());
        product.setCategory(this);
        product.setProductStock(product);
        products.add(product);
    }

    /**
     * Atualiza um produto existente na lista de produtos da categoria.
     *
     * @param newProduct novo estado do produto a ser atualizado
     * @throws NullPointerException       se {@code newProduct} for nulo
     * @throws ProductNotFoundException   se o produto com o ID fornecido não for encontrado
     * @throws ProductConflictException   se já existir outro produto com o mesmo nome
     */
    void updateProduct(Product newProduct) {

        Objects.requireNonNull(newProduct, "O produto não pode ser nulo");

        var currentProduct = getProductById(newProduct.getId());

        if (products.stream().anyMatch(p -> p.getName().equals(newProduct.getName()) && !p.getId().equals(newProduct.getId()))) {
            throw new ProductConflictException("Produto", "Nome", newProduct.getName());
        }

        currentProduct.setDetails(newProduct.getDetails());
        currentProduct.setPrice(newProduct.getPrice());
        currentProduct.setDisplayOrder(newProduct.getDisplayOrder());

        if (!currentProduct.getStockQuantity().equals(newProduct.getStockQuantity())) {
            currentProduct.setStockQuantity(newProduct.getStockQuantity());
            currentProduct.setActive(newProduct.isActive());
            currentProduct.markStockUpdateNow();
        }

        currentProduct.markUpdatedNow();
    }

    /**
     * Move um produto para uma nova categoria.
     *
     * @param product o produto a ser movida
     */
    void moveCategoryProduct(Product product) {

        product.setCategory(this);
        this.addProduct(product);
        product.markUpdatedNow();

    }


    /**
     * Remove um produto da categoria.
     *
     * @param productId ID do produto a ser removido
     * @throws NullPointerException se {@code productId} for nulo
     * @throws ProductNotFoundException se o produto não existir na categoria
     */
    void removeProduct(Long productId) {
        Objects.requireNonNull(productId, "O ID do produto não pode ser nulo");

        var product = getProductById(productId);

        products.remove(product);
    }

    /**
     * Atualiza o campo updatedAt com o horário atual.
     *
     * @throws BusinessException se o horário atual for menor ou igual ao createdAt
     */
    void markUpdatedNow() {
        this.auditInfo.setUpdatedAt(LocalDateTime.now());
    }
}
