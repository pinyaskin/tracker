package ru.pinyaskin.finance.security.util;

import io.jsonwebtoken.Claims;

public interface JwtCore {
    String generateToken(String subject);

    boolean isTokenExpired(String token);

    String getUsernameFromToken(String token);
}
