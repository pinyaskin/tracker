package ru.pinyaskin.tracker.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.pinyaskin.tracker.model.entity.User;
import ru.pinyaskin.tracker.repository.UserRepository;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerIT {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @Test
    void contextLoads() {
    }

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        User existingUser = new User();
        existingUser.setName("Patrick Jane");
        existingUser.setEmail("patrick@jane.com");
        existingUser.setPassword(passwordEncoder.encode("testPassword"));

        userRepository.save(existingUser);
    }

    @Test
    @DisplayName("Sign Up: user created")
    void signUp_shouldReturnOKAndValidToken() throws Exception {
        mvc.perform(
                post("/api/v1/auth/sign-up")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "John Black",
                                    "email": "john@mail.ru",
                                    "password": "ioo218S92!_0*"
                                }
                                """)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accessToken").isNotEmpty());
    }

    @Test
    @DisplayName("Falling Sign Up if User exists")
    void signUp_shouldThrowAnExceptionIfUserExists() throws Exception {
        mvc.perform(post("/api/v1/auth/sign-up")
                .contentType(APPLICATION_JSON)
                .content("""
                        {
                            "name": "Not Patrick Jane",
                            "email": "patrick@jane.com",
                            "password": "2ios90212ks"
                        }
                        """)
                )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$").value("Пользователь уже существует"));
    }

    @Test
    @DisplayName("sign in success")
    void signIn_shouldReturnOkAndToken() throws Exception {
        mvc.perform(post("/api/v1/auth/sign-in")
                .contentType(APPLICATION_JSON)
                .content("""
                        {
                            "email": "patrick@jane.com",
                            "password": "testPassword"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty());
    }

    @Test
    @DisplayName("sign in fails by bad credentials")
    void signIn_shouldThrowAnExceptionBadCredentials() throws Exception {
        mvc.perform(post("/api/v1/auth/sign-in")
                .contentType(APPLICATION_JSON)
                .content("""
                        {
                            "email": "psp@mail.ru",
                            "password": "someBadPass"
                        }
                        """))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$").value("Неверный Email или пароль"));
    }
}
