package ru.pinyaskin.finance.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.pinyaskin.finance.model.entity.User;
import ru.pinyaskin.finance.model.payload.request.SignUpRequest;
import ru.pinyaskin.finance.model.payload.response.AuthenticationResponse;
import ru.pinyaskin.finance.repository.UserRepository;
import ru.pinyaskin.finance.security.util.JwtCore;
import ru.pinyaskin.finance.service.impl.AuthenticationServiceImpl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceUnitTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtCore jwtCore;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    private User user;

    AuthenticationServiceUnitTest() {
        user = new User();
        user.setId(10L);
        user.setName("John");
        user.setEmail("john@mail.com");
        user.setPassword("password");
    }

    @Test
    void contextLoads() {
        assertThat(userRepository).isNotNull();
        assertThat(passwordEncoder).isNotNull();
        assertThat(jwtCore).isNotNull();
        assertThat(authenticationService).isNotNull();
        assertThat(user.getName()).isNotNull();
    }

    @Test
    void signUpShouldReturnValidToken() {
        // Подготовим запрос в сервис
        SignUpRequest request = new SignUpRequest();
        request.setName(user.getName());
        request.setEmail(user.getEmail());
        request.setPassword(user.getPassword());

        // Given
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
        Mockito.when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        Mockito.when(jwtCore.generateToken(user.getEmail())).thenReturn("token");

        // When
        AuthenticationResponse response = authenticationService.signUp(request);

        // Then
        assertThat(response)
                .isNotNull()
                .extracting(AuthenticationResponse::getAccessToken)
                .isEqualTo("token");

        Mockito.verify(userRepository).save(Mockito.any(User.class));
        Mockito.verify(passwordEncoder).encode(request.getPassword());
        Mockito.verify(jwtCore).generateToken(user.getEmail());
    }
}
