package ru.pinyaskin.finance.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.pinyaskin.finance.model.entity.User;
import ru.pinyaskin.finance.model.payload.request.SignInRequest;
import ru.pinyaskin.finance.model.payload.request.SignUpRequest;
import ru.pinyaskin.finance.model.payload.response.AuthenticationResponse;
import ru.pinyaskin.finance.repository.UserRepository;
import ru.pinyaskin.finance.security.util.JwtCore;
import ru.pinyaskin.finance.service.impl.AuthenticationServiceImpl;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

//@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceImplUT {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtCore jwtCore;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    // utils
    private User user;
    private SignUpRequest signUpRequest;
    private SignInRequest signInRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(10L);
        user.setName("John");
        user.setEmail("john@mail.com");
        user.setPassword("password");

        signUpRequest = new SignUpRequest();
        signUpRequest.setName(user.getName());
        signUpRequest.setEmail(user.getEmail());
        signUpRequest.setPassword(user.getPassword());

        signInRequest = new SignInRequest();
        signInRequest.setEmail(user.getEmail());
        signInRequest.setPassword(user.getPassword());
    }

    @Test
    @DisplayName("SignUp: success")
    void signUp_shouldReturnValidToken() {
        // Given
        Mockito.when(userRepository.save(any(User.class))).thenReturn(user);
        Mockito.when(passwordEncoder.encode(signUpRequest.getPassword())).thenReturn("encodedPassword");
        doReturn("token")
                .when(jwtCore)
                .generateToken(any(UserDetails.class));

        // When
        AuthenticationResponse response = authenticationService.signUp(signUpRequest);

        // Then
        assertThat(response)
                .isNotNull()
                .extracting(AuthenticationResponse::getAccessToken)
                .isEqualTo("token");

        verify(userRepository).save(any(User.class));
        verify(passwordEncoder).encode(signUpRequest.getPassword());
        verify(jwtCore).generateToken(any(UserDetails.class));
    }

    @Test
    @DisplayName("SignUp: user exists")
    void signUp_userIsExists_shouldThrowAnException() {
        // Given
        Mockito
                .when(userRepository.existsByEmail(signUpRequest.getEmail()))
                .thenThrow(new IllegalArgumentException("Пользователь уже существует"));

        // When
        Executable executable = () -> authenticationService.signUp(signUpRequest);

        assertThrows(IllegalArgumentException.class, executable);

        verify(userRepository).existsByEmail(signUpRequest.getEmail());
    }

    @Test
    @DisplayName("SignIn: success")
    void signIn_shouldReturnValidToken() {
        // Given
        doReturn(Optional.of(user))
                .when(userRepository)
                .findByEmail(signInRequest.getEmail());
        doReturn("token")
                .when(jwtCore)
                .generateToken(any(UserDetails.class));

        // When
        AuthenticationResponse response = authenticationService.signIn(signInRequest);

        // Then
        assertThat(response)
                .isNotNull()
                .extracting(AuthenticationResponse::getAccessToken)
                .isEqualTo("token");

        verify(userRepository).findByEmail(signInRequest.getEmail());
        verify(jwtCore).generateToken(any(UserDetails.class));
    }

    @Test
    @DisplayName("SignIn: bad credentials")
    void signIn_invalidPassword_shouldThrowBadCredentialsException() {
        // Given
        Mockito.when(authenticationManager.authenticate(
                        any(UsernamePasswordAuthenticationToken.class)
                ))
                .thenThrow(new BadCredentialsException("Bad Credentials"));

        // When
        Executable executable = () -> authenticationService.signIn(signInRequest);

        // Then
        assertThrows(AuthenticationException.class, executable);

        verify(authenticationManager).authenticate(
                any(UsernamePasswordAuthenticationToken.class)
        );
    }
}
