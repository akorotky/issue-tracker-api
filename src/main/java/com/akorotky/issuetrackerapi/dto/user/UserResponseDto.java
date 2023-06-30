package com.akorotky.issuetrackerapi.dto.user;

import org.springframework.hateoas.server.core.Relation;

import java.util.Set;

@Relation(collectionRelation = "users", itemRelation = "user")
public record UserResponseDto(
        Long id,
        String username,
        String email,
        Set<String> roles
) {
}
