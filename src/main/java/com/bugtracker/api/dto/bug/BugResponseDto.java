package com.bugtracker.api.dto.bug;

import com.bugtracker.api.dto.user.UserResponseDto;
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
