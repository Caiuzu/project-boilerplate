/**
 * Módulo de Usuário - Gerencia o cadastro e autenticação de clientes.
 * 
 * Responsabilidades:
 * - Cadastro e gestão de usuários
 * - Autenticação e autorização
 * - Gestão de preferências e histórico
 */
@ApplicationModule(
    displayName = "User Module",
    allowedDependencies = {
        "com.app.core.api.shared"
    }
)
package com.app.core.api.user;

import org.springframework.modulith.ApplicationModule;
