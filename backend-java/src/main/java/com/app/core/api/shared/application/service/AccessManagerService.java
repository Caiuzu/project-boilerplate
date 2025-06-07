package com.app.core.api.shared.application.service;

import com.app.core.api.shared.application.ports.in.AccessManager;
import com.app.core.api.shared.application.ports.out.AuthenticatedUserProvider;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

/**
 * Serviço que implementa a validação de acesso com base no ID e papel do usuário autenticado.
 */
@Component
public class AccessManagerService implements AccessManager {

    private final AuthenticatedUserProvider userProvider;

    /**
     * Construtor do serviço de validação de acesso.
     *
     * @param userProvider Provedor de dados do usuário autenticado.
     */
    public AccessManagerService(AuthenticatedUserProvider userProvider) {
        this.userProvider = userProvider;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validateAccess(Long userId) {
        Long currentUserId = userProvider.getUserId();
        if (currentUserId == null) {
            throw new AccessDeniedException("Usuário não autenticado.");
        }
        String role = userProvider.getUserRole();

        if (!currentUserId.equals(userId) && !"ADMIN".equalsIgnoreCase(role)) {
            throw new AccessDeniedException("Você não tem permissão para visualizar este recurso.");
        }
    }
}