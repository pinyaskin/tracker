package ru.pinyaskin.finance.security.util;

import io.jsonwebtoken.Jwts;

import java.security.Key;

public interface JwtCore {
    String generateToken(String subject);

    boolean isTokenExpired(String token);

    String getUsernameFromToken(String token);
}
