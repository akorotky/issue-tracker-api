package com.akorotky.issuetrackerapi.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.*;
import java.sql.Date;
import java.util.*;

@Service
@RequiredArgsConstructor
public class JwtTokenService {

    private final KeyPair accessTokenKeyPair = Keys.keyPairFor(SignatureAlgorithm.ES256);
    private final KeyPair refreshTokenKeyPair = Keys.keyPairFor(SignatureAlgorithm.RS256);

    @Value("${security.jwt.access-token.expire-length:900000}") // 15 min
    private Long accessTokenValidityInMillis;

    @Value("${security.jwt.refresh-token.expire-length:604800000}") // 1 week
    private Long refreshTokenValidityInMillis;

    public String generateToken(
            UserDetails userDetails,
            Key secretKey,
            SignatureAlgorithm signatureAlgorithm,
            Long validityInMillis
    ) {
        Date now = new Date(System.currentTimeMillis());
        Date expirationDate = new Date(now.getTime() + validityInMillis);
        List<String> authorities = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("authorities", authorities)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(secretKey, signatureAlgorithm)
                .compact();
    }

    public String generateAccessToken(UserDetails userDetails) {
        return generateToken(
                userDetails,
                accessTokenKeyPair.getPrivate(),
                SignatureAlgorithm.ES256,
                accessTokenValidityInMillis
        );
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return generateToken(
                userDetails,
                refreshTokenKeyPair.getPrivate(),
                SignatureAlgorithm.RS256,
                refreshTokenValidityInMillis
        );
    }

    public Key getTokenPublicKey(TokenType tokenType) {
        return switch (tokenType) {
            case ACCESS -> accessTokenKeyPair.getPublic();
            case REFRESH -> refreshTokenKeyPair.getPublic();
        };
    }

    public String extractUsernameFromToken(String token, TokenType tokenType) {
        return extractClaimsFromToken(token, tokenType).getSubject();
    }

    public Claims extractClaimsFromToken(String token, TokenType tokenType) {
        Key key = getTokenPublicKey(tokenType);
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
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
