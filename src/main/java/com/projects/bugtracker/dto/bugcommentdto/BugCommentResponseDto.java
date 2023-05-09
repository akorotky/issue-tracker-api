package com.projects.bugtracker.dto.bugcommentdto;

import com.projects.bugtracker.dto.userdto.UserResponseDto;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "comments", itemRelation = "comment")
public record BugCommentResponseDto(
        Long id,
        String body,
        UserResponseDto author,
        Long bugId
) {
}

