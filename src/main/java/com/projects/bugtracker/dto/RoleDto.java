package com.projects.bugtracker.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.projects.bugtracker.enums.RoleType;

public record RoleDto(

        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        Long id,

        RoleType name
) {
}
