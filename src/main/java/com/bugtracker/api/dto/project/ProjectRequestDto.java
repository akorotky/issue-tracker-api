package com.bugtracker.api.dto.project;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record ProjectRequestDto(
        @NotBlank(message = "Title must not be null or blank.")
        String title,

        String description,

        @NotBlank(message = "Visibility must not be null or blank.")
        @JsonProperty(value = "private")
        Boolean isPrivate
) {
}
