package com.app.core.api.user.domain.exceptions;

import com.app.core.api.shared.exception.ResourceNotFoundException;

/**
 * Exceção lançada quando um usuário não é encontrado
 */
public class UserNotFoundException extends ResourceNotFoundException {

    public UserNotFoundException(String message) {
        super(message);
    }
    public UserNotFoundException(String message, Long id) {
        super(message, id);
    }
}
