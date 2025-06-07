/**
 * Módulo de Pedidos - Núcleo do sistema, responsável pelo processo de montagem do pedido,
 * registo de saída e acompanhamento dos status.
 * 
 * Responsabilidades:
 * - Montagem e gestão de pedidos
 * - Acompanhamento de status do pedido
 * - Emissão de eventos para outros módulos 
 */
@ApplicationModule(
    displayName = "Order Module",
    allowedDependencies = {
        "com.app.core.api.product",
        "com.app.core.api.userId",
        "com.app.core.api.shared"
    }
)
package com.app.core.api.order;

import org.springframework.modulith.ApplicationModule;
