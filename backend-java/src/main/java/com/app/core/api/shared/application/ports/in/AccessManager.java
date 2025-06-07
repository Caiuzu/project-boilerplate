package com.app.core.api.shared.application.ports.in;

/**
 * Porta de entrada para validação de acesso do usuário autenticado.
 */
public interface AccessManager {

    /**
     * Valida se o usuário autenticado tem permissão para acessar os dados de um usuário específico.
     *
     * @param paymentUserId ID do usuário relacionado ao recurso sendo acessado.
     * @throws org.springframework.security.access.AccessDeniedException se o acesso não for permitido.
     */
    void validateAccess(Long paymentUserId);
}
