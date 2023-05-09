package com.bugtracker.api.dto.bugcommentdto;

import com.bugtracker.api.dto.userdto.UserResponseDto;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "comments", itemRelation = "comment")
public record BugCommentResponseDto(
        Long id,
        String body,
        UserResponseDto author,
        Long bugId
) {
}

