package com.projects.bugtracker.dto.userdto;

import com.projects.bugtracker.dto.roledto.RoleDto;
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
