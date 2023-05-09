package com.projects.bugtracker.dto.bugdto;

import com.projects.bugtracker.dto.userdto.UserResponseDto;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "bugs", itemRelation = "bug")
public record BugResponseDto(
        Long id,
        String title,
        String description,
        UserResponseDto author,
        Long projectId
) {
}
