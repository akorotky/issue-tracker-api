package com.bugtracker.api.dto.bugcomment;

import com.bugtracker.api.dto.user.UserResponseDto;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "comments", itemRelation = "comment")
public record BugCommentResponseDto(
        Long id,
        String body,
        UserResponseDto author,
        Long bugId
) {
}

