package com.projects.bugtracker.dto.projectdto;

import jakarta.validation.constraints.NotBlank;

public record ProjectRequestDto(
        @NotBlank(message = "Title must not be null or blank.")
        String title,
        String description
) {
}
