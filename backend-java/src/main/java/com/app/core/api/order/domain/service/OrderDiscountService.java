package com.app.core.api.order.domain.service;

import com.app.core.api.order.domain.exceptions.OrderException;
import com.app.core.api.order.domain.model.Order;
import com.app.core.api.user.domain.exceptions.UserNotFoundException;
import com.app.core.api.user.domain.ports.out.UserRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Serviço de domínio responsável pela aplicação de descontos em pedidos com base no tempo de cadastro do usuário.
 */
@Data
@Service
public class OrderDiscountService {

    /**
     * Repositório para acesso aos dados do usuário.
     */
    private final UserRepository userRepository;

    /**
     * Construtor que inicializa o serviço com o repositório de usuários.
     *
     * @param userRepository repositório para acesso aos dados do usuário
     */
    public OrderDiscountService(
            UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }

    /**
     * Aplica um desconto no pedido baseado no tempo de cadastro do cliente.
     * <p>
     * O desconto é calculado como 2% ao ano desde o ano de criação do usuário até o ano atual.
     * O desconto máximo aplicado será menor que 96%.
     * Caso o cliente não seja encontrado, uma exceção {@link OrderException} é lançada.
     *
     * @param order pedido no qual o desconto será aplicado
     * @throws OrderException se o cliente do pedido não for encontrado
     */
    public void applyDiscount(Order order) {

        var user = userRepository.findById(order.getUserId());

        if (user.isEmpty()) {
            throw new UserNotFoundException("Cliente do pedido não encontrado");
        }

        var yearCreateUser = user.get().getCreatedAt().getYear();
        var currentYear = LocalDateTime.now().getYear();

        var percent = (currentYear - yearCreateUser) * 2;

        if (percent > 0 && percent < 96) {
            order.applyDiscount(percent);
        }

    }

}