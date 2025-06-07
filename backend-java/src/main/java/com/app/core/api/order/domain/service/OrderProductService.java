package com.app.core.api.order.domain.service;

import com.app.core.api.catalog.domain.exceptions.ProductNotFoundException;
import com.app.core.api.catalog.domain.ports.out.CatalogRepository;
import com.app.core.api.order.domain.exceptions.OrderException;
import com.app.core.api.order.domain.exceptions.OrderItemException;
import com.app.core.api.order.domain.model.OrderItem;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Serviço de domínio responsável pela validação de produtos associados aos itens de um pedido.
 */
@Data
@Service
public class OrderProductService {

    private final CatalogRepository catalogRepository;

    /**
     * Cria uma instância do serviço com o repositório de catálogo.
     *
     * @param catalogRepository o repositório de catálogos utilizado para busca de produtos
     */
    public OrderProductService(CatalogRepository catalogRepository) {
        this.catalogRepository = catalogRepository;
    }

    /**
     * Valida os produtos associados aos itens do pedido.
     * <p>
     * Para cada item do pedido, verifica se o produto existe no catálogo e se o preço informado
     * no item corresponde ao preço atual do produto no catálogo.
     *
     * @param orderItems a lista de itens do pedido a serem validados
     * @throws OrderException se algum produto não for encontrado no catálogo
     * @throws OrderItemException se o preço do item do pedido não corresponder ao preço do produto
     */
    public void validateOrderItemProduct(List<OrderItem> orderItems) {

        for (OrderItem orderItem : orderItems) {

            var catalog = catalogRepository.findByProductId(orderItem.getProductId());

            if (catalog.isEmpty()) {
                throw new ProductNotFoundException("O produto do item do pedido não existe");
            }

            var productOrderItem = catalog.get().getProductById(orderItem.getProductId());

            if (!productOrderItem.getName().equals(orderItem.getName())) {
                throw new OrderItemException("O nome do produto do item diverge do nome do produto cadastrado");
            }
            else if (productOrderItem.getPrice().compareTo(orderItem.getPrice()) != 0) {
                throw new OrderItemException("O preço unitário do item do pedido diverge do preço do produto");
            }
            else if (!productOrderItem.isActive()) {
                throw new OrderItemException("O pedido não pode possuir produtos inativos");
            }
            else if (!productOrderItem.categoryisActive()) {
                throw new OrderItemException("A categoria do produto do pedido não pode estar inativa");
            }
            else if (productOrderItem.getStockQuantity() < orderItem.getQuantity()) {
                throw new OrderItemException(String.format("Quantidade insuficiente em estoque para o produto: %s", productOrderItem.getName()));
            }

        }
    }
}