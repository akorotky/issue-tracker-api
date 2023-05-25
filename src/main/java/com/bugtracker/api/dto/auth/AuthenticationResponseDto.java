package com.bugtracker.api.dto.auth;

public record AuthenticationResponseDto(
        String accessToken,
        String refreshToken
) {
}
