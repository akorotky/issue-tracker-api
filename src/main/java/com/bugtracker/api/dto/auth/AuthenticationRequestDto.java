package com.bugtracker.api.dto.auth;

public record AuthenticationRequestDto(
        String username,
        String password
) {
}
