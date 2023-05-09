package com.bugtracker.api.dto.authdto;

public record AuthenticationRequestDto(
        String username,
        String password
) {
}
