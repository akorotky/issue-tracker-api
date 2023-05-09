package com.bugtracker.api.dto.bugcommentdto;

import jakarta.validation.constraints.NotBlank;

public record BugCommentRequestDto(
        @NotBlank(message = "Comment body must not be null or blank")
        String body,
        Long bugId
) {
}
