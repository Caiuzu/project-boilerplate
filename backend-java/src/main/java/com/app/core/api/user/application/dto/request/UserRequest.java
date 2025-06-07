package com.app.core.api.user.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * DTO para requisições de criação/atualização de usuários
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "UserRequest", description = "DTO para criação ou atualização de usuários")
public class UserRequest {

    @Schema(description = "Identifica se o usuário é guest ou não", example = "false", required = false)
    private boolean guest;

    @Schema(description = "Nome completo do usuário", example = "João Silva", required = false)
    private String name;

    @Builder.Default
    @Schema(description = "Apelido do usuário", example = "Jão", required = false)
    private String username = null;

    @Builder.Default
    @Email(message = "O email deve ser válido")
    @Schema(description = "Email do usuário", example = "joao.silva@email.com", required = false)
    private String email = null;

    @Schema(description = "Senha do usuário", example = "João Silva", required = false)
    private String password;

    @Builder.Default
    @Schema(description = "Documento do usuário (apenas números ou formatado)", example = "845.641.190-60", required = false)
    private String document = null;
}
