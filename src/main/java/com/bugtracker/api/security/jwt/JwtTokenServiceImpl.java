package com.bugtracker.api.security.jwt;

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
import java.sql.Date;

@Service
@RequiredArgsConstructor
public class JwtTokenServiceImpl implements JwtTokenService {

    private final KeyPair accessTokenKeyPair = Keys.keyPairFor(SignatureAlgorithm.ES256);
    private final KeyPair refreshTokenKeyPair = Keys.keyPairFor(SignatureAlgorithm.RS256);

    @Value("${security.jwt.access-token.expire-length:900000}") // 15 min
    private long accessTokenValidityInMillis;

    @Value("${security.jwt.refresh-token.expire-length:604800000}") // 1 week
    private long refreshTokenValidityInMillis;

    private final UserDetailsService userDetailsService;

    @Override
    public String generateToken(UserDetails userDetails, Key secretKey, SignatureAlgorithm signatureAlgorithm) {
        Date now = new Date(System.currentTimeMillis());
        Date expirationDate = new Date(now.getTime() + refreshTokenValidityInMillis);

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("authorities", userDetails.getAuthorities())
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(secretKey, signatureAlgorithm)
                .compact();
    }

    @Override
    public String generateAccessToken(UserDetails userDetails) {
        return generateToken(userDetails, accessTokenKeyPair.getPrivate(), SignatureAlgorithm.ES256);
    }

    @Override
    public String generateRefreshToken(UserDetails userDetails) {
        return generateToken(userDetails, refreshTokenKeyPair.getPrivate(), SignatureAlgorithm.RS256);
    }

    @Override
    public String generateAccessToken(String refreshToken) {
        try {
            verifyToken(refreshToken, TokenType.REFRESH);
        } catch (JwtException e) {
            return null;
        }

        Authentication authentication = getAuthentication(refreshToken, TokenType.REFRESH);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return generateAccessToken(userDetails);
    }

    @Override
    public Authentication getAuthentication(String token, TokenType tokenType) {
        String username = extractUsernameFromToken(token, tokenType);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @Override
    public Key getTokenPublicKey(TokenType tokenType) {
        return switch (tokenType) {
            case ACCESS -> accessTokenKeyPair.getPublic();
            case REFRESH -> refreshTokenKeyPair.getPublic();
        };
    }

    @Override
    public String extractUsernameFromToken(String token, TokenType tokenType) {
        Key key = getTokenPublicKey(tokenType);
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }

    @Override
    public String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring("Bearer ".length());
        }
        return null;
    }

    @Override
    public void verifyToken(String token, TokenType tokenType) throws JwtException {
        try {
            Jwts.parserBuilder().setSigningKey(getTokenPublicKey(tokenType)).build().parseClaimsJws(token);
        } catch (JwtException e) {
            throw new JwtException("Expired or invalid JWT token");
        }
    }

}
