package com.bugtracker.api.dto.authdto;

public record AuthenticationResponseDto(
        String accessToken,
        String refreshToken
) {
}
