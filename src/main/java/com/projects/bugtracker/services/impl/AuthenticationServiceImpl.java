package com.projects.bugtracker.services.impl;

import com.projects.bugtracker.dto.authdto.AuthenticationRequestDto;
import com.projects.bugtracker.dto.authdto.AuthenticationResponseDto;
import com.projects.bugtracker.dto.tokendto.AccessTokenRequestDto;
import com.projects.bugtracker.dto.userdto.UserRequestDto;
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
    public AuthenticationResponseDto authenticate(AuthenticationRequestDto authenticationRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequestDto.username(),
                        authenticationRequestDto.password()
                )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String accessToken = jwtTokenService.generateAccessToken(userDetails);
        String refreshToken = jwtTokenService.generateRefreshToken(userDetails);
        return new AuthenticationResponseDto(accessToken, refreshToken);
    }

    @Override
    public void register(UserRequestDto userRequestDto) {
        userService.createUser(userRequestDto);
    }

    @Override
    public String refreshAccessToken(AccessTokenRequestDto accessTokenRequestDto) {
        return jwtTokenService.generateAccessToken(accessTokenRequestDto.refreshToken());
    }

}
