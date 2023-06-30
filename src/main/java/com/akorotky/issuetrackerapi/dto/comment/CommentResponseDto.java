package com.akorotky.issuetrackerapi.dto.comment;

import com.akorotky.issuetrackerapi.dto.user.UserResponseDto;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "comments", itemRelation = "comment")
public record CommentResponseDto(
        Long id,
        String body,
        UserResponseDto author,
        Long issueId
) {
}

