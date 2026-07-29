package com.recharge.phone.config;

import java.sql.Date;
import java.util.Base64;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final SecretKey key;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;

    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-expiration}") long accessTokenExpiration,
            @Value("${jwt.refresh-token-expiration}") long refreshTokenExpiration) {
        this.key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    public String generateAcessToken(String userId, String userEmail) {
        return BuildToken(userId, userEmail, accessTokenExpiration, "access");

    }

    public String generateRefreshToken(String userId, String userEmail) {
        return BuildToken(userId, userEmail, refreshTokenExpiration, "refresh");
    }

    public long getAccessTokenExpiration() {
        return accessTokenExpiration;
    }

    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractEmail(String token){
        return parseToken(token).getSubject();
    }

    public boolean isTokenValid(String token) {
        try {
            parseToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private String BuildToken(String userId, String userEmail, long expiration, String type) {
        Date now = new Date(System.currentTimeMillis());
        String subject = userEmail;

        return Jwts.builder()
                .subject(subject)
                .claim("userId", userId)
                .claim("type", type)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + expiration))
                .signWith(key).compact();
    }
}
