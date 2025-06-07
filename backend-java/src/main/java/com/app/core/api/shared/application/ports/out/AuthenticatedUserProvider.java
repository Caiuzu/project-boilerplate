package com.app.core.api.shared.application.ports.out;

/**
 * Porta de saída responsável por fornecer informações do usuário autenticado no sistema.
 */
public interface AuthenticatedUserProvider {

    /**
     * Obtém o ID do usuário autenticado.
     *
     * @return ID do usuário autenticado.
     */
    Long getUserId();

    /**
     * Obtém o papel (role) do usuário autenticado.
     *
     * @return Papel do usuário (ex: ADMIN, CLIENTE).
     */
    String getUserRole();
}