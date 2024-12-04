package ru.pinyaskin.tracker.security.util;

import org.junit.jupiter.api.Test;
import ru.pinyaskin.tracker.model.entity.User;
import ru.pinyaskin.tracker.security.UserPrincipal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class JwtCoreTest {
    @Test
    void shouldReturnValidKey() {
        JwtCore jwt = new JwtCoreImpl("secret-key", 600000);
        User user = new User();
        user.setEmail("john@mail.ru");

        String token = jwt.generateToken(new UserPrincipal(user));
        boolean isExpired = jwt.isTokenExpired(token);
        String username = jwt.getUsernameFromToken(token);

        assertThat(token).isNotNull();
        assertThat(isExpired).isFalse();
        assertThat(username).isEqualTo("john@mail.ru");
    }
}
