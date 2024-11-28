package ru.pinyaskin.finance.security.util;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JwtCoreTest {
    @Test
    public void shouldReturnValidKey() {
        JwtCore jwt = new JwtCoreImpl("secret-key");
        String token = jwt.generateToken("user");
        System.out.println(token);
    }
}
