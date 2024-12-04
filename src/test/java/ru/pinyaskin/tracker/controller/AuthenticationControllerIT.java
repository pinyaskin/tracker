package ru.pinyaskin.tracker.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.pinyaskin.tracker.model.entity.User;
import ru.pinyaskin.tracker.repository.UserRepository;

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

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();

        passwordEncoder = NoOpPasswordEncoder.getInstance();

        User existingUser = new User();
        existingUser.setName("Patrick Jane");
        existingUser.setEmail("patrick@jane.com");
        existingUser.setPassword("testPassword");

        userRepository.save(existingUser);
    }

    @Test
    @DisplayName("Sign Up: user created")
    void signUp_shouldReturnOKAndValidToken() throws Exception {
        mvc.perform(
                post("/api/v1/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
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
                .contentType(MediaType.APPLICATION_JSON)
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
}
