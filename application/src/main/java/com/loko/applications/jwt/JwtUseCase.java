package com.loko.applications.jwt;

import java.util.Map;

import javax.crypto.SecretKey;

public interface JwtUseCase {
    String extractUsername(String token);

    String generateToken(String username);

    String generateToken(Map<String, ?> extractClaims, String username, long expirationTime);

    boolean isTokenExpired(String token);

    SecretKey getSignInKey();
    boolean isTokenValid(String token,String username);
    String generateRefreshToken(String username);
}
