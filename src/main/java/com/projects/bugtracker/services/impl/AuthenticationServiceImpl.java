package com.projects.bugtracker.services.impl;

import com.projects.bugtracker.dto.*;
import com.projects.bugtracker.services.AuthenticationService;
import com.projects.bugtracker.services.JwtTokenService;
import com.projects.bugtracker.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final JwtTokenService jwtTokenService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.username(),
                        authenticationRequest.password()
                )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String accessToken = jwtTokenService.generateAccessToken(userDetails);
        String refreshToken = jwtTokenService.generateRefreshToken(userDetails);
        return new AuthenticationResponse(accessToken, refreshToken);
    }

    @Override
    public void register(UserDto userDto) {
        userService.createUser(userDto);
    }

    @Override
    public String refreshAccessToken(AccessTokenRequest accessTokenRequest) {
        return jwtTokenService.generateAccessToken(accessTokenRequest.refreshToken());
    }

}
