package com.loko.infrastructure.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.loko.applications.jwt.JwtUseCase;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService implements JwtUseCase {

    // private static final Logger logger = LoggerFactory.getLogger(JwtService.class);
    @Value(value = "${application.security.jwt.secret-key}")
    private String secretKey;
    @Value(value = "${application.security.jwt.expiration}")
    private long jwtExpiration;
    @Value(value = "${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;


    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    @Override
    public String generateToken(String username) {
        return generateToken(new HashMap<>(), username, jwtExpiration);
    }

    @Override
    public String generateToken(Map<String, ?> extractClaims, String username, long expirationTime) {
        return Jwts.builder()
                .claims(extractClaims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSignInKey())
                .compact();
    }

    @Override
    public boolean isTokenValid(String token, String username) {
        return (extractUsername(token).equals(username)) && !isTokenExpired(token);
    }

    @Override
    public boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    
    // private Date extraExpired(String token) {
    //     return extractClaim(token, Claims::getExpiration);
    // }

    
    private Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(getSignInKey()).build().parseSignedClaims(token).getPayload();
    }

    @Override
    public SecretKey getSignInKey() {
        byte[] key = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(key);
    }

    @Override
    public String generateRefreshToken(String username) {
        return generateToken(new HashMap<>(), username, refreshExpiration);
    }

}
