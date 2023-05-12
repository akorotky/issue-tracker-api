package com.bugtracker.api.security.jwt;

import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;

public interface JwtTokenService {

    String generateToken(UserDetails userDetails, Key key, SignatureAlgorithm signatureAlgorithm);

    String generateAccessToken(UserDetails userDetails);

    String generateAccessToken(String refreshToken);

    String generateRefreshToken(UserDetails userDetails);

    Authentication getAuthentication(String token, TokenType tokenType);

    Key getTokenPublicKey(TokenType tokenType);

    String extractUsernameFromToken(String token, TokenType tokenType);

    String extractTokenFromRequest(HttpServletRequest request);

    void verifyToken(String token, TokenType tokenType);
}
