package com.app.core.api.user.application.ports.in;

import com.app.core.api.user.domain.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Porta de entrada para operações relacionadas a usuários
 */
public interface UserUseCase {
    
    /**
     * Cadastra um novo usuário
     * @param user Usuário a ser cadastrado
     * @return Usuário cadastrado com ID gerado
     */
    User createUser(User user);

    
    /**
     * Atualiza um usuário existente
     * @param id ID do usuário a ser atualizado
     * @param user Usuário com dados atualizados
     * @return Usuário atualizado
     */
    User updateUser(Long id, User user);
    
    /**
     * Busca um usuário por ID
     * @param id ID do usuário
     * @return Optional contendo o usuário ou vazio se não encontrado
     */
    Optional<User> getUserById(Long id);
    
    /**
     * Busca um cliente por DOCUMENT
     * @param document DOCUMENT do usuário
     * @return Optional contendo o usuário ou vazio se não encontrado
     */
    Optional<User> getUserByDocument(String document);
    
    /**
     * Lista todos os usuários
     * @return Lista de usuários
     */
    List<User> getAllUsers();
    
    /**
     * Remove um usuário
     * @param id ID do usuário a ser removido
     */
    void deleteUser(Long id);

    /**
     * Realiza o login de um usuário
     * @param email Email do usuário
     * @param rawPassword Senha em texto puro
     * @return Usuário autenticado ou lança exceção se falhar
     */
    User login(String email, String rawPassword);
}