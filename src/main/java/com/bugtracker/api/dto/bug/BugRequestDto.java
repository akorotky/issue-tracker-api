package com.bugtracker.api.dto.bug;

import jakarta.validation.constraints.NotBlank;

public record BugRequestDto(
        @NotBlank(message = "Title must not be null or blank.")
        String title,
        String description,
        Long projectId
) {
}
