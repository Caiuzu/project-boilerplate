package com.app.core.api.user.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para requisições de Login de usuários
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "LoginRequest", description = "DTO para login de usuários")
public class LoginRequest {

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "O email deve ser válido")
    @Schema(description = "Email do usuário", example = "admin@fastapp.com", required = true)
    private String email;

    @NotBlank(message = "A senha é obrigatória")
    @Schema(description = "Senha do usuário", example = "admin123", required = true)
    private String password;
}
