package com.bugtracker.api.services.impl;

import com.bugtracker.api.dto.userdto.UserRequestDto;
import com.bugtracker.api.dto.authdto.AuthenticationRequestDto;
import com.bugtracker.api.dto.authdto.AuthenticationResponseDto;
import com.bugtracker.api.dto.tokendto.AccessTokenRequestDto;
import com.bugtracker.api.services.AuthenticationService;
import com.bugtracker.api.services.JwtTokenService;
import com.bugtracker.api.services.UserService;
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
