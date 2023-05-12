package com.bugtracker.api.dto.roledto;

import com.bugtracker.api.entities.role.Role;

public interface RoleDtoMapper {

    default String mapRoleToString(Role role) {
        return role.getName().name();
    }
}
