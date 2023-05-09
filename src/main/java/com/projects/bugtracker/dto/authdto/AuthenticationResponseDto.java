package com.projects.bugtracker.dto.authdto;

public record AuthenticationResponseDto(
        String accessToken,
        String refreshToken
) {
}
