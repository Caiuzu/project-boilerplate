package com.app.core.api.user.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "UserResponse", description = "DTO para resposta com informações de clientes")
public class UserResponse {
    
    @Schema(description = "Identificador único do usuário", example = "1")
    private Long id;
    
    @Schema(description = "Nome completo do usuário", example = "João Silva")
    private String name;

    @Schema(description = "Apelido do usuário", example = "Jão")
    private String username;
    
    @Schema(description = "Email do usuário", example = "joao.silva@email.com")
    private String email;
    
    @Schema(description = "DOCUMENT do usuário", example = "123.456.789-00")
    private String document;

    @Schema(description = "Indica se o usuário está ativo", example = "true")
    private boolean active;

    @Schema(description = "Indica a role do usuário", example = "2")
    private Integer role;
    
    @Schema(description = "Data e hora de criação do registro", example = "2023-01-01T10:00:00")
    private LocalDateTime createdAt;
    
    @Schema(description = "Data e hora da última atualização", example = "2023-01-10T11:30:00")
    private LocalDateTime updatedAt;

    @Schema(description = "Token JWT", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private transient String token;
    

} 