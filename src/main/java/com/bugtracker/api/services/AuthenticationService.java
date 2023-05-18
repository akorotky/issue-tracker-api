package com.bugtracker.api.services;

import com.bugtracker.api.dto.authdto.AuthenticationRequestDto;
import com.bugtracker.api.dto.authdto.AuthenticationResponseDto;
import com.bugtracker.api.dto.userdto.UserRequestDto;
import com.bugtracker.api.dto.tokendto.AccessTokenRequestDto;
import org.springframework.security.core.Authentication;

public interface AuthenticationService {

    AuthenticationResponseDto authenticateUser(AuthenticationRequestDto authenticationRequestDto);

    Authentication getAuthenticationFromUsernamePassword(String username, String password);

    void registerUser(UserRequestDto userRequestDto);

    String refreshAccessToken(AccessTokenRequestDto accessTokenRequestDto);
}
