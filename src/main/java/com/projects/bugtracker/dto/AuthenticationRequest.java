package com.projects.bugtracker.dto;

public record AuthenticationRequest(
        String username,
        String password
) {
}
