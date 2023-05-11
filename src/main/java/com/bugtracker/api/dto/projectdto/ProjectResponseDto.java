package com.bugtracker.api.dto.projectdto;

import com.bugtracker.api.dto.userdto.UserResponseDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "projects", itemRelation = "project")
@JsonPropertyOrder({"id", "title", "description", "private", "owner"})
public record ProjectResponseDto(
        Long id,
        String title,
        String description,
        @JsonProperty(value = "private")
        Boolean isPrivate,
        UserResponseDto owner
) {
}
