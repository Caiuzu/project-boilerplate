package com.app.core.api.user.domain.vo;

import lombok.Getter;

@Getter
public enum RoleType {
    ADMIN(1),
    USER(2),
    GUEST(3);

    private final int id;

    RoleType(int id) {
        this.id = id;
    }

    public static RoleType fromId(int id) {
        for (RoleType role : values()) {
            if (role.id == id) return role;
        }
        throw new IllegalArgumentException("Role inválida: " + id);
    }
}