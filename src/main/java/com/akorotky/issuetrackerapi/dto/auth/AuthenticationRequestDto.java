package com.akorotky.issuetrackerapi.dto.auth;

public record AuthenticationRequestDto(
        String username,
        String password
) {
}
