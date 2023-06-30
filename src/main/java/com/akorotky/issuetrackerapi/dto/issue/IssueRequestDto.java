package com.akorotky.issuetrackerapi.dto.issue;

import jakarta.validation.constraints.NotBlank;

public record IssueRequestDto(
        @NotBlank(message = "Title must not be null or blank.")
        String title,
        String description,
        Long projectId
) {
}
