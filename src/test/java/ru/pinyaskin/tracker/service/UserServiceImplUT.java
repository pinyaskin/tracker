package ru.pinyaskin.tracker.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.pinyaskin.tracker.model.entity.User;
import ru.pinyaskin.tracker.repository.UserRepository;
import ru.pinyaskin.tracker.service.impl.UserServiceImpl;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

public class UserServiceImplUT {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    // utils
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setEmail("john@mail.com");
        user.setName("John");
        user.setPassword("testPassword");
    }

    @Test
    @DisplayName("getUserByEmail: success")
    void getUserByEmail_shouldReturnValidUser() {
        // Given
        doReturn(Optional.of(user))
                .when(userRepository)
                .findByEmail(user.getEmail());

        // When
        User testUser = userService.getUserByEmail(user.getEmail());

        // Then
        assertThat(testUser).isNotNull()
                .extracting(User::getEmail)
                .isEqualTo(user.getEmail());

        verify(userRepository).findByEmail(user.getEmail());
    }

    @Test
    @DisplayName("getUserByEmail: user not found")
    void getUserByEmail_userNotFound_shouldThrowException() {
        // Given
        doReturn(Optional.empty())
                .when(userRepository)
                .findByEmail(user.getEmail());

        // When
        Executable executable = () -> userService.getUserByEmail(user.getEmail());

        // Then
        assertThrows(UsernameNotFoundException.class, executable);

        verify(userRepository).findByEmail(user.getEmail());
    }
}
