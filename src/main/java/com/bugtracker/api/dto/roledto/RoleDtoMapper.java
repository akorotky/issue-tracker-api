package com.bugtracker.api.dto.roledto;

import com.bugtracker.api.entities.Role;

public interface RoleDtoMapper {

    RoleDto toDto(Role role);

    Role toEntity(RoleDto roleDto);
}
