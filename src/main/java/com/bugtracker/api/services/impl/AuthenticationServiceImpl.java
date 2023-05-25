package com.bugtracker.api.services.impl;

import com.bugtracker.api.dto.user.UserRequestDto;
import com.bugtracker.api.dto.auth.AuthenticationRequestDto;
import com.bugtracker.api.dto.token.AccessTokenRequestDto;
import com.bugtracker.api.security.jwt.TokenType;
import com.bugtracker.api.services.AuthenticationService;
import com.bugtracker.api.security.jwt.JwtTokenService;
import com.bugtracker.api.services.UserService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final JwtTokenService jwtTokenService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final UserDetailsService userDetailsService;

    @Override
    public Map<String, String> authenticateUser(AuthenticationRequestDto authenticationRequestDto) {
        Authentication authentication = getAuthenticationFromUsernamePassword(
                authenticationRequestDto.username(),
                authenticationRequestDto.password()
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", jwtTokenService.generateAccessToken(userDetails));
        tokens.put("refreshToken", jwtTokenService.generateRefreshToken(userDetails));
        return tokens;
    }

    @Override
    public Authentication getAuthenticationFromUsernamePassword(String username, String password) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
    }

    @Override
    public void registerUser(UserRequestDto userRequestDto) {
        userService.createUser(userRequestDto);
    }

    @Override
    public String refreshAccessToken(AccessTokenRequestDto accessTokenRequestDto) throws JwtException {
        jwtTokenService.verifyToken(accessTokenRequestDto.refreshToken(), TokenType.REFRESH);
        String username = jwtTokenService.extractUsernameFromToken(accessTokenRequestDto.refreshToken(), TokenType.REFRESH);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return jwtTokenService.generateAccessToken(userDetails);
    }
}
