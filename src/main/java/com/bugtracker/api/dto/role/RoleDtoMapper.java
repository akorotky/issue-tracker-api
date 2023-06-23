package com.bugtracker.api.dto.role;

import com.bugtracker.api.entity.role.Role;

public interface RoleDtoMapper {

    default String mapRoleToString(Role role) {
        return role.getName().name();
    }
}
