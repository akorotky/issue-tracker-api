package com.bugtracker.api.dto.roledto;

import com.bugtracker.api.entities.role.RoleType;

public record RoleDto(
        RoleType name
) {
}
