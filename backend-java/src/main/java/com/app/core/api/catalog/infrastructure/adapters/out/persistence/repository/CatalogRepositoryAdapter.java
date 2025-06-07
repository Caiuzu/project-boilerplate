package com.app.core.api.catalog.infrastructure.adapters.out.persistence.repository;

import com.app.core.api.catalog.domain.ports.out.CatalogRepository;
import com.app.core.api.catalog.domain.model.Catalog;
import com.app.core.api.catalog.infrastructure.adapters.out.persistence.entity.CatalogEntity;
import com.app.core.api.catalog.infrastructure.adapters.out.persistence.mapper.CatalogEntityMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adaptador que implementa a porta de saída CatalogRepository
 * responsável por operações sobre o agregado Catálogo.
 */
@Component
public class CatalogRepositoryAdapter implements CatalogRepository {

    private final SpringDataCatalogRepository springDataCatalogRepository;
    private final CatalogEntityMapper catalogEntityMapper;

    public CatalogRepositoryAdapter(
            SpringDataCatalogRepository springDataCatalogRepository,
            CatalogEntityMapper catalogEntityMapper
    ) {
        this.springDataCatalogRepository = springDataCatalogRepository;
        this.catalogEntityMapper = catalogEntityMapper;
    }

    /**
     * Salva o agregado Catálogo.
     *
     * @param catalog Agregado Catálogo a ser salvo
     * @return Agregado salvo com identificadores atualizados
     */
    @Override
    public Catalog save(Catalog catalog) {
        CatalogEntity entity = catalogEntityMapper.toEntity(catalog);
        CatalogEntity saved = springDataCatalogRepository.save(entity);
        return catalogEntityMapper.toDomain(saved);
    }

    /**
     * Busca um catálogo pelo ID.
     *
     * @param id ID do catálogo
     * @return Optional contendo o catálogo ou vazio se não encontrado
     */
    @Override
    public Optional<Catalog> findById(Long id) {
        return springDataCatalogRepository.findById(id)
                .map(catalogEntityMapper::toDomain);
    }

    /**
     * Busca um catálogo pelo nome.
     *
     * @param name nome do catálogo
     * @return Optional contendo o catálogo ou vazio se não encontrado
     */
    @Override
    public Optional<Catalog> findByName(String name) {
        return springDataCatalogRepository.findByName(name)
                .map(catalogEntityMapper::toDomain);
    }

    /**
     * Lista todos os catálogos persistidos.
     *
     * @return Lista de catálogos
     */
    @Override
    public List<Catalog> findAll() {
        return springDataCatalogRepository.findAll().stream()
                .map(catalogEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    /**
     * Verifica se existe um catalogo com um determinado ID.
     *
     * @param id ID do catálogo
     * @return true se existir um catálogo com um determinado ID, false caso contrário
     */
    @Override
    public boolean existsById(Long id) {
        return springDataCatalogRepository.existsById(id);
    }

    /**
     * Verifica se existe um catalogo com um determinado nome.
     *
     * @param name Nome do catálogo
     * @return true se existir um catálogo com um determinado nome, false caso contrário
     */
    @Override
    public boolean existsByName(String name) {
        return springDataCatalogRepository.existsByName(name);
    }

    /**
     * Verifica se existe outro catálogo com o mesmo nome, mas com ID diferente.
     *
     * @param name Nome do catálogo
     * @param id   ID do catálogo que está sendo atualizado
     * @return true se existir outro catálogo com o mesmo nome e ID diferente, false caso contrário
     */
    @Override
    public boolean existsByNameAndIdNot(String name, Long id) {
        return springDataCatalogRepository.existsByNameAndIdNot(name, id);
    }

    @Override
    public boolean existsCategoryByCatalogId(Long catalogId) {
        return springDataCatalogRepository.existsCategoryByCatalogId(catalogId);
    }

    /**
     * Remove um catálogo com base em seu ID.
     *
     * @param id ID do catálogo a ser removido
     */
    @Override
    public void delete(Long id) {
        springDataCatalogRepository.deleteById(id);
    }

    @Override
    public Optional<Catalog> findByProductId(Long productId) {
        return springDataCatalogRepository.findByProductId(productId).map(catalogEntityMapper::toDomain);
    }
}