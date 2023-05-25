package com.bugtracker.api.services;

import com.bugtracker.api.dto.auth.AuthenticationRequestDto;
import com.bugtracker.api.dto.user.UserRequestDto;
import com.bugtracker.api.dto.token.AccessTokenRequestDto;
import org.springframework.security.core.Authentication;

import java.util.Map;

public interface AuthenticationService {

    Map<String, String> authenticateUser(AuthenticationRequestDto authenticationRequestDto);

    Authentication getAuthenticationFromUsernamePassword(String username, String password);

    void registerUser(UserRequestDto userRequestDto);

    String refreshAccessToken(AccessTokenRequestDto accessTokenRequestDto);
}
