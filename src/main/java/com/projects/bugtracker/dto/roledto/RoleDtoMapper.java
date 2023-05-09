package com.projects.bugtracker.dto.roledto;

import com.projects.bugtracker.entities.Role;

public interface RoleDtoMapper {

    RoleDto toDto(Role role);

    Role toEntity(RoleDto roleDto);
}
