package com.bugtracker.api.dto.role;

import com.bugtracker.api.entities.role.Role;

public interface RoleDtoMapper {

    default String mapRoleToString(Role role) {
        return role.getName().name();
    }
}
