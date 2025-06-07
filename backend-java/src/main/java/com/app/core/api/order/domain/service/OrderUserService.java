package com.app.core.api.order.domain.service;

import com.app.core.api.order.domain.model.Order;
import com.app.core.api.user.domain.exceptions.UserNotFoundException;
import com.app.core.api.user.domain.ports.out.UserRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

/**
 * Serviço de domínio responsável por aplicar regras relacionadas ao usuário em um pedido.
 * <p>
 * Atualmente, valida se o usuário associado ao pedido está presente.
 * Caso contrário, associa o usuário do tipo {@code GUEST}.
 */
@Data
@Service
public class OrderUserService {

    /**
     * Repositório para acesso aos dados do usuário.
     */
    private final UserRepository userRepository;

    /**
     * Cria uma instância de {@code OrderUserService} com o repositório de usuários fornecido.
     *
     * @param userRepository o repositório utilizado para buscar dados de usuários
     */
    public OrderUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Valida se o pedido possui um ID de usuário associado.
     * <p>
     * Caso não tenha, tenta associar automaticamente o usuário do tipo {@code GUEST}.
     * Se não for possível encontrar esse usuário, lança uma exceção.
     *
     * @param order o pedido a ser validado e atualizado
     * @throws UserNotFoundException se o usuário do tipo {@code GUEST} não for encontrado
     */
    public void validateGuestCustomer(Order order) {
        if (order.getUserId() == null) {
            var userGuest = userRepository.findFirstByGuestTrue();

            if (userGuest.isEmpty()) {
                throw new UserNotFoundException("Usuário GUEST não encontrado");
            }

            order.setUserId(userGuest.get().getId());
        }
    }
}