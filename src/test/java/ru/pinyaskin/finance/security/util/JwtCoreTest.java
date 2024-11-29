package ru.pinyaskin.finance.security.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class JwtCoreTest {
    @Test
    void shouldReturnValidKey() {
        JwtCore jwt = new JwtCoreImpl("secret-key", 60000);
        String token = jwt.generateToken("user");
        boolean isExpired = jwt.isTokenExpired(token);
        String username = jwt.getUsernameFromToken(token);

        assertThat(token).isNotNull();
        assertThat(isExpired).isFalse();
        assertThat(username).isEqualTo("user");
    }
}
