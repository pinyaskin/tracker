package ru.pinyaskin.finance.security.util;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.token.Sha512DigestUtils;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;

@Component
public class JwtCoreImpl implements JwtCore {
    private Key key;

    @Value("${app.jwt.expiration-time}")
    private long expirationTime;

    public JwtCoreImpl(@Value("${app.jwt.key}") String rawKey) {
        byte[] bytesKey = Sha512DigestUtils.sha(rawKey);
        this.key = new SecretKeySpec(bytesKey, "HMACSHA256");
    }

    @Override
    public String generateToken(String subject) {
        return Jwts
                .builder()
                .subject(subject)
                .expiration(getExpirationDate())
                .issuedAt(new Date())
                .signWith(key)
                .compact();
    }

    @Override
    public boolean isTokenExpired(String token) {
        return false;
    }

    @Override
    public String getUsernameFromToken(String token) {
        return null;
    }

    private Date getExpirationDate() {
        Date date = new Date();
        date.setTime(date.getTime() + expirationTime);
        return date;
    }
}
