package com.bugtracker.api.service;

import com.bugtracker.api.dto.auth.AuthenticationRequestDto;
import com.bugtracker.api.dto.token.AccessTokenRequestDto;
import org.springframework.security.core.Authentication;

import java.util.Map;

public interface AuthenticationService {

    Map<String, String> authenticateUser(AuthenticationRequestDto authenticationRequestDto);

    Authentication getAuthenticationFromUsernamePassword(String username, String password);

    String refreshAccessToken(AccessTokenRequestDto accessTokenRequestDto);
}
