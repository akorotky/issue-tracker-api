package com.projects.bugtracker.services;

import com.projects.bugtracker.dto.authdto.AuthenticationRequestDto;
import com.projects.bugtracker.dto.authdto.AuthenticationResponseDto;
import com.projects.bugtracker.dto.tokendto.AccessTokenRequestDto;
import com.projects.bugtracker.dto.userdto.UserRequestDto;

public interface AuthenticationService {

    AuthenticationResponseDto authenticate(AuthenticationRequestDto authenticationRequestDto);

    void register(UserRequestDto userRequestDto);

    String refreshAccessToken(AccessTokenRequestDto accessTokenRequestDto);
}
