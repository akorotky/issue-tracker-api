package com.bugtracker.api.dto.role;

import com.bugtracker.api.entities.role.RoleType;

public record RoleDto(
        RoleType name
) {
}
