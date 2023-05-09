package com.bugtracker.api.services;

import com.bugtracker.api.dto.authdto.AuthenticationRequestDto;
import com.bugtracker.api.dto.authdto.AuthenticationResponseDto;
import com.bugtracker.api.dto.userdto.UserRequestDto;
import com.bugtracker.api.dto.tokendto.AccessTokenRequestDto;

public interface AuthenticationService {

    AuthenticationResponseDto authenticate(AuthenticationRequestDto authenticationRequestDto);

    void register(UserRequestDto userRequestDto);

    String refreshAccessToken(AccessTokenRequestDto accessTokenRequestDto);
}
