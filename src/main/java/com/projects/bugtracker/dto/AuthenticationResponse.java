package com.projects.bugtracker.dto;

public record AuthenticationResponse(
        String accessToken,
        String refreshToken
) {
}
