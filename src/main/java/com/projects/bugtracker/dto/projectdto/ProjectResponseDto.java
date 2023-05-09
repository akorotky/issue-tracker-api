package com.projects.bugtracker.dto.projectdto;

import com.projects.bugtracker.dto.userdto.UserResponseDto;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "projects", itemRelation = "project")
public record ProjectResponseDto(
        Long id,
        String title,
        String description,
        UserResponseDto owner
) {
}
