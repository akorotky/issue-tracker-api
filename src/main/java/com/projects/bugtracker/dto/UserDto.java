package com.projects.bugtracker.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import org.springframework.hateoas.server.core.Relation;

import java.util.Set;

@Relation(collectionRelation = "users", itemRelation = "user")
public record UserDto(

        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        Long id,

        @NotBlank(message = "Username must not be null or blank.")
        @Length(min = 1, max = 15)
        String username,

        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        @NotBlank(message = "Password must not be null or blank.")
        @Length(min = 8, max = 30)
        String password,

        @NotBlank(message = "Email must not be null or blank.")
        String email,

        Set<RoleDto> roles
) {
}
