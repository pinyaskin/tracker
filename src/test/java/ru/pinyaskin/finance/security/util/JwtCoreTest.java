package ru.pinyaskin.finance.security.util;

import org.junit.jupiter.api.Test;

public class JwtCoreTest {
    @Test
    void shouldReturnValidKey() {
        JwtCore jwt = new JwtCoreImpl("secret-key", 60000);
        String token = jwt.generateToken("user");
        System.out.println(token);
        boolean isExpired = jwt.isTokenExpired(token);
        System.out.println(isExpired);
    }
}
