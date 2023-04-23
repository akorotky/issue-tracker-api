package com.projects.bugtracker.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "comments", itemRelation = "comment")
public record BugCommentDto(

        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        Long id,

        @NotBlank(message = "Comment body must not be null or blank")
        String body,

        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        UserDto author,

        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        Long bugId
) {
}
