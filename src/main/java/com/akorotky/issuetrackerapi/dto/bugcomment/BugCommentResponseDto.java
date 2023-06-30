package com.akorotky.issuetrackerapi.dto.bugcomment;

import com.akorotky.issuetrackerapi.dto.user.UserResponseDto;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "comments", itemRelation = "comment")
public record BugCommentResponseDto(
        Long id,
        String body,
        UserResponseDto author,
        Long bugId
) {
}

