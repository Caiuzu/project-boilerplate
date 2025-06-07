/**
 * Módulo de Produtos - Gerencia o catálogo de produtos disponíveis para venda, organizados por categorias.
 * 
 * Responsabilidades:
 * - CRUD de produtos e categorias
 * - Gestão de imagens de produtos com integração com blob storage
 * - Emissão de eventos para outros módulos
 */
@ApplicationModule(
    displayName = "Product Module",
    allowedDependencies = {
        "com.app.core.api.shared"
    }
)
package com.app.core.api.catalog;

import org.springframework.modulith.ApplicationModule;
