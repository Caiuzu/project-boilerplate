package com.app.core.api.user.infrastructure.adapters.in.controller;

import com.app.core.api.user.application.ports.in.UserUseCase;
import com.app.core.api.user.domain.model.User;
import com.app.core.api.user.application.dto.request.LoginRequest;
import com.app.core.api.user.application.dto.request.UserRequest;
import com.app.core.api.user.application.dto.response.UserResponse;
import com.app.core.api.user.application.mapper.UserDtoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gerenciamento de usuários
 */
@RestController
@RequestMapping("/users")
@Tag(name = "Usuários", description = "API para gerenciamento de usuários")
public class UserController {

    private final UserUseCase userUseCase;
    private final UserDtoMapper userDtoMapper;

    public UserController(UserUseCase userUseCase, UserDtoMapper userDtoMapper) {
        this.userUseCase = userUseCase;
        this.userDtoMapper = userDtoMapper;
    }
    /**
     * Lista todos os usuários
     * @return Lista de usuários
     */
    @GetMapping
    @Operation(summary = "Listar todos os usuários", description = "Retorna uma lista com todos os usuários cadastrados", security = @SecurityRequirement(name = "bearer-key") )
    @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = UserResponse.class))))
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<User> users = userUseCase.getAllUsers();
        return ResponseEntity.ok(userDtoMapper.toResponseList(users));
    }

    /**
     * Busca um usuário por ID
     * @param id ID do usuário
     * @return Usuário encontrado ou 404
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuário por ID", description = "Retorna um usuário específico pelo seu ID", security = @SecurityRequirement(name = "bearer-key"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                    content = @Content)
    })
    public ResponseEntity<UserResponse> getUserById(
            @Parameter(description = "ID do usuário", example = "1", required = true)
            @PathVariable Long id) {
        return userUseCase.getUserById(id)
                .map(user -> ResponseEntity.ok(userDtoMapper.toResponse(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Busca um usuário por DOCUMENT
     * @param document DOCUMENT do usuário
     * @return Usuário encontrado ou 404
     */
    @GetMapping("/document/{document}")
    @Operation(summary = "Buscar usuário por DOCUMENT", description = "Retorna um usuário específico pelo seu DOCUMENT", security = @SecurityRequirement(name = "bearer-key"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                    content = @Content)
    })
    public ResponseEntity<UserResponse> getUserByDocument(
            @Parameter(description = "DOCUMENT do usuário", example = "123.456.789-00", required = true)
            @PathVariable String document) {
        return userUseCase.getUserByDocument(document)
                .map(user -> ResponseEntity.ok(userDtoMapper.toResponse(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Cria um novo usuário
     * @param request Dados do usuário
     * @return Usuário criado
     */
    @PostMapping
    @Operation(summary = "Criar novo usuário", description = "Cria um novo usuário com os dados fornecidos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content)
    })
    public ResponseEntity<UserResponse> createUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados do usuário a ser criado", required = true,
                    content = @Content(schema = @Schema(implementation = UserRequest.class)))
            @Valid @RequestBody UserRequest request) {
        try {
            User user = userDtoMapper.toDomain(request);

            User createdUser = userUseCase.createUser(user);
            return new ResponseEntity<>(userDtoMapper.toResponse(createdUser), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Realiza o login de um usuário com email e senha")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content)
    })
    public ResponseEntity<UserResponse> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados do login", required = true,
                    content = @Content(schema = @Schema(implementation = LoginRequest.class)))
            @Valid @RequestBody LoginRequest request) {
        try {

            User loggedUser = userUseCase.login(request.getEmail(), request.getPassword());
            return new ResponseEntity<>(userDtoMapper.toResponse(loggedUser), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Atualiza um usuário existente
     * @param id ID do usuário
     * @param request Dados atualizados
     * @return Usuário atualizado ou 404
     */
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar usuário", description = "Atualiza os dados de um usuário existente", security = @SecurityRequirement(name = "bearer-key"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                    content = @Content)
    })
    public ResponseEntity<UserResponse> updateUser(
            @Parameter(description = "ID do usuário", example = "1", required = true)
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados atualizados do usuário", required = true,
                    content = @Content(schema = @Schema(implementation = UserRequest.class)))
            @Valid @RequestBody UserRequest request) {
        
        return userUseCase.getUserById(id)
                .map(existingUser -> {
                    userDtoMapper.updateDomainFromRequest(request, existingUser);
                    User updatedUser = userUseCase.updateUser(id, existingUser);
                    return ResponseEntity.ok(userDtoMapper.toResponse(updatedUser));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Remove um usuário
     * @param id ID do usuário
     * @return 204 sem conteúdo ou 404
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Remover usuário", description = "Remove um usuário pelo seu ID", security = @SecurityRequirement(name = "bearer-key"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuário removido com sucesso",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                    content = @Content)
    })
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID do usuário", example = "1", required = true)
            @PathVariable Long id) {
        return userUseCase.getUserById(id)
                .map(user -> {
                    userUseCase.deleteUser(id);
                    return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
                })
                .orElse(ResponseEntity.notFound().build());
    }
} 