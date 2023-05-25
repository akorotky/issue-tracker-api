package com.bugtracker.api.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;

public interface JwtTokenService {

    String generateToken(UserDetails userDetails, Key key, SignatureAlgorithm signatureAlgorithm, Long validityInMillis);

    String generateAccessToken(UserDetails userDetails);

    String generateRefreshToken(UserDetails userDetails);

    Key getTokenPublicKey(TokenType tokenType);

    Claims extractClaimsFromToken(String token, TokenType tokenType);

    String extractUsernameFromToken(String token, TokenType tokenType);

    String extractTokenFromRequest(HttpServletRequest request);

    void verifyToken(String token, TokenType tokenType);
}
