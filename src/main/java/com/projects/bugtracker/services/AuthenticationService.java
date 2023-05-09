package com.projects.bugtracker.services;

import com.projects.bugtracker.dto.*;

public interface AuthenticationService {

    AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest);

    void register(UserDto userDto);

    String refreshAccessToken(AccessTokenRequest accessTokenRequest);
}
