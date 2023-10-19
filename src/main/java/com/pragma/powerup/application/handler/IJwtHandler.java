package com.pragma.powerup.application.handler;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

public interface IJwtHandler {
    String extractUserName(String token);
    Claims extractAllClaims(String token);
    Key getSignInKey();
    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);
    String generateToken(Map<String, Object> extraClaims, UserDetails userDetails);
    String generateToken(UserDetails userDetails);
    boolean isTokenValid(String token, UserDetails userDetails);
    boolean isTokenExpired(String token);
    Date extractExpiration(String token);
}
