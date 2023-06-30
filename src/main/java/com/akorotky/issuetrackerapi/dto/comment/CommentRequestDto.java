package com.akorotky.issuetrackerapi.dto.comment;

import jakarta.validation.constraints.NotBlank;

public record CommentRequestDto(
        @NotBlank(message = "Comment body must not be null or blank")
        String body,
        Long issueId
) {
}
