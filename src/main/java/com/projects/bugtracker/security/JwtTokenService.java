package com.projects.bugtracker.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.security.*;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtTokenService {

    private final KeyPair accessTokenKeyPair = Keys.keyPairFor(SignatureAlgorithm.ES256);
    private final KeyPair refreshTokenKeyPair = Keys.keyPairFor(SignatureAlgorithm.RS256);

    @Value("${security.jwt.access-token.expire-length:900000}") // 15 min
    private long accessTokenValidityInMillis;

    @Value("${security.jwt.refresh-token.expire-length:604800000}") // 1 week
    private long refreshTokenValidityInMillis;

    private final UserDetailsService userDetailsService;

    public String generateAccessToken(UserDetails userDetails) {
        Claims claims = Jwts.claims().setSubject(userDetails.getUsername());
        claims.put("authorities", userDetails.getAuthorities());

        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + accessTokenValidityInMillis);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(accessTokenKeyPair.getPrivate(), SignatureAlgorithm.ES256)
                .compact();
    }

    public String generateAccessToken(String refreshToken) {
        try {
            verifyToken(refreshToken, TokenType.REFRESH);
        } catch (JwtException e) {
            return null;
        }

        Authentication authentication = getAuthentication(refreshToken, TokenType.REFRESH);
        Claims claims = Jwts.claims().setSubject(authentication.getName());
        claims.put("authorities", authentication.getAuthorities());

        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + accessTokenValidityInMillis);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(accessTokenKeyPair.getPrivate(), SignatureAlgorithm.ES256)
                .compact();
    }

    public String generateRefreshToken(UserDetails userDetails) {
        Claims claims = Jwts.claims().setSubject(userDetails.getUsername());
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + refreshTokenValidityInMillis);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(refreshTokenKeyPair.getPrivate(), SignatureAlgorithm.RS256)
                .compact();
    }

    public Authentication getAuthentication(String token, TokenType tokenType) {
        String username = extractUsernameFromToken(token, tokenType);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public Key getTokenPublicKey(TokenType tokenType) {
        return switch (tokenType) {
            case ACCESS -> accessTokenKeyPair.getPublic();
            case REFRESH -> refreshTokenKeyPair.getPublic();
        };
    }

    public String extractUsernameFromToken(String token, TokenType tokenType) {
        Key key = getTokenPublicKey(tokenType);
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }

    public String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring("Bearer ".length());
        }
        return null;
    }

    public void verifyToken(String token, TokenType tokenType) {
        try {
            Jwts.parserBuilder().setSigningKey(getTokenPublicKey(tokenType)).build().parseClaimsJws(token);
        } catch (JwtException e) {
            throw new JwtException("Expired or invalid JWT token");
        }
    }

}
