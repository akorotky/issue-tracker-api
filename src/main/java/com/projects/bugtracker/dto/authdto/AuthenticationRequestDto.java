package com.projects.bugtracker.dto.authdto;

public record AuthenticationRequestDto(
        String username,
        String password
) {
}
