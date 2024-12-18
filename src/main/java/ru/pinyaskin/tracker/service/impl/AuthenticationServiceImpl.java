package ru.pinyaskin.tracker.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.pinyaskin.tracker.exception.SignUpException;
import ru.pinyaskin.tracker.model.entity.User;
import ru.pinyaskin.tracker.model.payload.request.SignInRequest;
import ru.pinyaskin.tracker.model.payload.request.SignUpRequest;
import ru.pinyaskin.tracker.model.payload.response.AuthenticationResponse;
import ru.pinyaskin.tracker.repository.UserRepository;
import ru.pinyaskin.tracker.security.UserPrincipal;
import ru.pinyaskin.tracker.security.util.JwtCore;
import ru.pinyaskin.tracker.service.AuthenticationService;

@Component
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtCore jwt;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponse signIn(SignInRequest request) {
        /*
        Отдаем наши credentials в authentication Manager.
        Manager сам найдет юзера и проверит пароль.
        Если данные неверны - бросит AuthenticationException.
        */
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        ));

        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

        // Генерируем токен
        String accessToken = jwt.generateToken(new UserPrincipal(user));

        var response = new AuthenticationResponse();
        response.setAccessToken(accessToken);

        return response;
    }

    @Override
    @Transactional
    public AuthenticationResponse signUp(SignUpRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new SignUpException("Пользователь уже существует");
        }

        // Создаем User и кладем в бд
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user = userRepository.save(user);

        // Генерируем JWT-токен
        String token = jwt.generateToken(new UserPrincipal(user));

        // Формируем ответ
        AuthenticationResponse response = new AuthenticationResponse();
        response.setAccessToken(token);

        return response;
    }


}
