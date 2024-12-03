package ru.pinyaskin.finance.security.util;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtCore {
    // Переделать на UserDetails
    String generateToken(UserDetails userDetails);

    boolean isTokenExpired(String token);

    String getUsernameFromToken(String token);
}
