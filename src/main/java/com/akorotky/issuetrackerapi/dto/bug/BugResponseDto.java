package com.akorotky.issuetrackerapi.dto.bug;

import com.akorotky.issuetrackerapi.dto.user.UserResponseDto;
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
