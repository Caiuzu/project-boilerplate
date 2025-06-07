package com.app.core.api.user.application.mapper;

import com.app.core.api.shared.mapper.AuditInfoMapper;
import com.app.core.api.user.domain.model.User;
import com.app.core.api.user.application.dto.request.UserRequest;
import com.app.core.api.user.application.dto.response.UserResponse;
import org.mapstruct.*;

import java.util.List;

/**
 * Mapper que converte entre DTOs e entidades de domínio para usuários
 */
@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {RoleTypeMapper.class, AuditInfoMapper.class}
)
public interface UserDtoMapper {
    
    /**
     * Converte entidade de domínio para DTO de resposta
     * @param user Entidade de domínio
     * @return DTO de resposta
     */
    @Mapping(source = "role.id", target = "role")
    @Mapping(source = "auditInfo", target = "createdAt", qualifiedByName = "mapCreatedAt")
    @Mapping(source = "auditInfo", target = "updatedAt", qualifiedByName = "mapUpdatedAt")
    UserResponse toResponse(User user);
    
    /**
     * Converte lista de entidades de domínio para lista de DTOs de resposta
     * @param users Lista de entidades de domínio
     * @return Lista de DTOs de resposta
     */
    List<UserResponse> toResponseList(List<User> users);
    
    /**
     * Converte DTO de requisição para entidade de domínio
     * @param request DTO de requisição
     * @return Entidade de domínio
     */
    @Mapping(target = "id", ignore = true)
    User toDomain(UserRequest request);

    
    /**
     * Atualiza uma entidade de domínio com os dados de um DTO de requisição
     * @param request DTO de requisição com os dados atualizados
     * @param user Entidade de domínio a ser atualizada
     */
    @Mapping(target = "id", ignore = true)
    void updateDomainFromRequest(UserRequest request, @MappingTarget User user);
} 