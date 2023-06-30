package com.akorotky.issuetrackerapi.dto.auth;

public record AuthenticationResponseDto(
        String accessToken,
        String refreshToken
) {
}
