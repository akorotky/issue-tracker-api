package com.bugtracker.api.dto.projectdto;

import com.bugtracker.api.dto.userdto.UserResponseDto;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "projects", itemRelation = "project")
public record ProjectResponseDto(
        Long id,
        String title,
        String description,
        UserResponseDto owner
) {
}
