package com.app.core.api.shared.infrastructure.adapters.out.auth;

import com.app.core.api.shared.application.ports.out.AuthenticatedUserProvider;
import com.app.core.api.shared.exception.JwtException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

/**
 * Implementação de {@link AuthenticatedUserProvider} que extrai informações do usuário a partir do contexto JWT.
 */
@Component
public class JwtAuthenticatedUserProvider implements AuthenticatedUserProvider {

    /**
     * {@inheritDoc}
     */
    @Override
    public Long getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof String)) {
            throw new JwtException("Usuário não autenticado com JWT");
        }

        Object details = authentication.getDetails();
        if (details instanceof Long id) {
            return id;
        }

        throw new JwtException("ID do usuário não encontrado no contexto de autenticação.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new JwtException("Usuário não autenticado com JWT");
        }

        return authentication.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .map(role -> role.replace("ROLE_", ""))
                .orElseThrow(() -> new JwtException("Role não encontrada no token"));
    }
}