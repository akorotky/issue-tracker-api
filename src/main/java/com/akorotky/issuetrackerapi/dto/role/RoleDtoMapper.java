package com.akorotky.issuetrackerapi.dto.role;

import com.akorotky.issuetrackerapi.entity.role.Role;

public interface RoleDtoMapper {

    default String mapRoleToString(Role role) {
        return role.getName().name();
    }
}
