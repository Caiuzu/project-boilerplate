package com.app.core.api.user.application.services;

import com.app.core.api.shared.exception.ResourceConflictException;
import com.app.core.api.shared.exception.ResourceNotFoundException;
import com.app.core.api.shared.infrastructure.adapters.out.logging.CustomLogger;
import com.app.core.api.shared.infrastructure.adapters.out.auth.JwtTokenProvider;
import com.app.core.api.user.domain.vo.RoleType;
import com.app.core.api.user.application.ports.in.UserUseCase;
import com.app.core.api.user.domain.ports.out.UserRepository;
import com.app.core.api.user.domain.model.Role;
import com.app.core.api.user.domain.model.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementação do caso de uso de Usuário.
 */
@Service
public class UserService implements UserUseCase {

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final CustomLogger logger;
    private final JwtTokenProvider jwtService;


    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, JwtTokenProvider jwtService) {
        this.userRepository = userRepository;
        this.logger = CustomLogger.getLogger(getClass());
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }


    @Override
    @Transactional
    public User createUser(User user) {
        if (user.isGuest()) {
            User guestUser = userRepository.findByRoleId((long) RoleType.GUEST.getId())
                    .orElseThrow(() -> new RuntimeException("Usuário GUEST não encontrado no banco."));
            String token = jwtService.generateToken(guestUser);
            guestUser.setToken(token);
            return guestUser;
        }

        user.validateInternalState();

        if (user.hasDocument()) {
            Optional<User> existingByDocument = userRepository.findByDocument(user.getDocument());
            if (existingByDocument.isPresent()) {
                User existingUser = existingByDocument.get();
                String token = jwtService.generateToken(existingUser);
                existingUser.setToken(token);
                return existingUser;
            }
        }

        if (user.hasEmail()) {
            Optional<User> existingByEmail = userRepository.findByEmail(user.getEmail());
            if (existingByEmail.isPresent()) {
                User existingUser = existingByEmail.get();
                String token = jwtService.generateToken(existingUser);
                existingUser.setToken(token);
                return existingUser;
            }
        }

        assignDefaultRole(user);

        if (user.hasPassword()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        user.activate();
        User saved = userRepository.save(user);
        String token = jwtService.generateToken(saved);
        saved.setToken(token);
        logger.debug("Usuário criado com sucesso. ID: {}", saved.getId());
        return saved;
    }

    @Override
    @Transactional
    public User updateUser(Long id, User user) {
        logger.debug("Atualizando usuário com ID: {}", id);
        
        Optional<User> existingUser = userRepository.findById(id);
        
        if (existingUser.isEmpty()) {
            logger.warn("Usuário não encontrado com ID: {}", id);
            throw new ResourceNotFoundException("Usuário", "id", id);
        }
        
        if (!existingUser.get().getDocument().equals(user.getDocument())) {
            Optional<User> userWithSameDocument = userRepository.findByDocument(user.getDocument());
            if (userWithSameDocument.isPresent() && !userWithSameDocument.get().getId().equals(id)) {
                logger.warn("Tentativa de atualizar usuário para documento já existente: {}", user.getDocument());
                throw new ResourceConflictException("usuário", "documento", user.getDocument());
            }
        }
        
        user.setId(id);
        User updatedUser = userRepository.save(user);
        logger.debug("Usuário atualizado com sucesso. ID: {}", updatedUser.getId());
        
        return updatedUser;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserById(Long id) {
        logger.debug("Buscando usuário com ID: {}", id);
        Optional<User> user = userRepository.findById(id);
        
        if (user.isEmpty()) {
            logger.debug("Usuário não encontrado com ID: {}", id);
        } else {
            logger.debug("Usuário encontrado. Nome: {}", user.get().getName());
        }
        
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserByDocument(String document) {
        logger.debug("Buscando usuários com documento: {}", document);
        return userRepository.findByDocument(document);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        logger.debug("Buscando todos os usuários");
        List<User> users = userRepository.findAll();
        logger.debug("Total de usuários encontrados: {}", users.size());
        return users;
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        logger.debug("Excluindo usuário com ID: {}", id);
        
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            logger.warn("Tentativa de excluir usuário inexistente. ID: {}", id);
            throw new ResourceNotFoundException("Usuário", "id", id);
        }
        
        userRepository.delete(id);
        logger.debug("Usuário excluído com sucesso. ID: {}", id);
    }



    @Override
    @Transactional(readOnly = true)
    public User login(String email, String rawPassword) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            logger.warn("Usuário não encontrado para o email: {}", email);
            throw new ResourceNotFoundException("Usuário", "email", email);
        }

        User user = optionalUser.get();

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            logger.warn("Senha incorreta para o email: {}", email);
            throw new RuntimeException("Email ou senha inválidos");
        }

        String token = jwtService.generateToken(user);
        user.setToken(token);

        logger.debug("Usuário autenticado com sucesso. ID: {}", user.getId());
        return user;
    }

    private void assignDefaultRole(User user) {
        if (user.getRole()==null || user.getRole().getId()==0) {
            Role r = new Role();
            r.setId((long)RoleType.USER.getId());
            r.setName(RoleType.USER.name());
            user.setRole(r);
        }
    }
}