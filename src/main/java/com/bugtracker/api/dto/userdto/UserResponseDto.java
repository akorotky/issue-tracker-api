package com.bugtracker.api.dto.userdto;

import com.bugtracker.api.dto.roledto.RoleDto;
import org.springframework.hateoas.server.core.Relation;

import java.util.Set;

@Relation(collectionRelation = "users", itemRelation = "user")
public record UserResponseDto(
        Long id,
        String username,
        String email,
        Set<RoleDto> roles
) {
}
