package com.app.core.api.user.application.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(name = "UserTokenResponse", description = "Resposta com dados do usuário e token JWT")
public class UserTokenResponse {

    @Schema(description = "Dados do usuário")
    private UserResponse user;

    @Schema(description = "Token de autenticação JWT", example = "eyJhbGciOiJIUzI1NiIsInR...")
    private String token;
}
