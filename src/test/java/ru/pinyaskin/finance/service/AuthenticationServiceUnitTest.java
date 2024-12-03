package ru.pinyaskin.finance.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.pinyaskin.finance.model.entity.User;
import ru.pinyaskin.finance.model.payload.request.SignUpRequest;
import ru.pinyaskin.finance.model.payload.response.AuthenticationResponse;
import ru.pinyaskin.finance.repository.UserRepository;
import ru.pinyaskin.finance.security.UserPrincipal;
import ru.pinyaskin.finance.security.util.JwtCore;
import ru.pinyaskin.finance.service.impl.AuthenticationServiceImpl;

import java.sql.SQLIntegrityConstraintViolationException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceUnitTest {
    // mocking
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtCore jwtCore;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    // utils
    private final User user;
    private final SignUpRequest request;

    AuthenticationServiceUnitTest() {
        user = new User();
        user.setId(10L);
        user.setName("John");
        user.setEmail("john@mail.com");
        user.setPassword("password");

        request = new SignUpRequest();
        request.setName(user.getName());
        request.setEmail(user.getEmail());
        request.setPassword(user.getPassword());
    }

    @Test
    @DisplayName("Успешная регистрация")
    void signUp_shouldReturnValidToken() {
        // Given
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
        Mockito.when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        Mockito.when(jwtCore.generateToken(new UserPrincipal(user))).thenReturn("token");

        // When
        AuthenticationResponse response = authenticationService.signUp(request);

        // Then
        assertThat(response)
                .isNotNull()
                .extracting(AuthenticationResponse::getAccessToken)
                .isEqualTo("token");

        Mockito.verify(userRepository).save(Mockito.any(User.class));
        Mockito.verify(passwordEncoder).encode(request.getPassword());
        Mockito.verify(jwtCore).generateToken(new UserPrincipal(user));
    }

    @Test
    @DisplayName("Ошибка: пользователь уже существует")
    void signUp_userIsExists_shouldThrowAnException() {
        // Given
        Mockito
                .when(userRepository.existsByEmail(request.getEmail()))
                .thenThrow(new IllegalArgumentException("Пользователь уже существует"));

        // When
        Executable executable = () -> authenticationService.signUp(request);

        assertThrows(IllegalArgumentException.class, executable);

        Mockito.verify(userRepository).existsByEmail(request.getEmail());
    }
}
