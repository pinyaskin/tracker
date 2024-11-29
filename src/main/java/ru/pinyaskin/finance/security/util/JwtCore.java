package ru.pinyaskin.finance.security.util;

public interface JwtCore {
    String generateToken(String subject);

    boolean isTokenExpired(String token);

    String getUsernameFromToken(String token);
}
