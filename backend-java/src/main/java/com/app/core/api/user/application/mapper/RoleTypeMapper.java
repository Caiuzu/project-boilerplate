package com.app.core.api.user.application.mapper;

import com.app.core.api.user.domain.vo.RoleType;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public class RoleTypeMapper {

    @Named("intToRoleType")
    public RoleType intToRoleType(Integer id) {
        return RoleType.fromId(id);
    }

    @Named("roleTypeToInt")
    public Integer roleTypeToInt(RoleType roleType) {
        return roleType != null ? roleType.getId() : null;
    }
}
