package ru.pinyaskin.tracker.security.util;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.token.Sha512DigestUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

@Component
public class JwtCoreImpl implements JwtCore {
    private SecretKey key;

    private long expirationTime;

    public JwtCoreImpl(@Value("${app.jwt.key}") String rawKey,
                       @Value("${app.jwt.expiration-time}") long expirationTime) {
        byte[] bytesKey = Sha512DigestUtils.sha(rawKey);
        this.key = new SecretKeySpec(bytesKey, "HMACSHA256");
        this.expirationTime = expirationTime;
    }

    @Override
    public String generateToken(UserDetails userDetails) {
        return Jwts
                .builder()
                .subject(userDetails.getUsername())
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .issuedAt(new Date(System.currentTimeMillis()))
                .signWith(key)
                .compact();
    }

    @Override
    public boolean isTokenExpired(String token) {
        try {
            Date expDate = extractClaims(token).getPayload().getExpiration();
            return expDate.before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    @Override
    public String getUsernameFromToken(String token) {
        return extractClaims(token).getPayload().getSubject();
    }

    private Jws<Claims> extractClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);
    }
}
