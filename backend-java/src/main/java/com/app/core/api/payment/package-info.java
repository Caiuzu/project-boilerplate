/**
 * Módulo de Pagamento - Gerencia a integração com gateways de pagamento e o controle
 * das transações financeiras.
 * 
 * Responsabilidades:
 * - Processamento de pagamentos
 * - Integração com gateways de pagamento
 * - Emissão de eventos para outros módulos 
 */
@ApplicationModule(
    displayName = "Payment Module",
    allowedDependencies = {
        "com.app.core.api.order",
        "com.app.core.api.shared"
    }
)
package com.app.core.api.payment;

import org.springframework.modulith.ApplicationModule;
