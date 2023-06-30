package com.akorotky.issuetrackerapi.dto.issue;

import com.akorotky.issuetrackerapi.dto.user.UserResponseDto;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "issues", itemRelation = "issue")
public record IssueResponseDto(
        Long id,
        String title,
        String description,
        UserResponseDto author,
        Long projectId
) {
}
