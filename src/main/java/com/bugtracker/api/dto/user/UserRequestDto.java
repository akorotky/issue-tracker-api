package com.bugtracker.api.dto.user;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record UserRequestDto(
        @NotBlank(message = "Username must not be null or blank.")
        @Length(min = 1, max = 15)
        String username,
        @NotBlank(message = "Password must not be null or blank.")
        @Length(min = 8, max = 30)
        String password,
        @NotBlank(message = "Email must not be null or blank.")
        String email
) {
}
