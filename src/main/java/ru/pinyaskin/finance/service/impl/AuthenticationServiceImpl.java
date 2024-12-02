package ru.pinyaskin.finance.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.pinyaskin.finance.model.entity.User;
import ru.pinyaskin.finance.model.payload.request.SignInRequest;
import ru.pinyaskin.finance.model.payload.request.SignUpRequest;
import ru.pinyaskin.finance.model.payload.response.AuthenticationResponse;
import ru.pinyaskin.finance.repository.UserRepository;
import ru.pinyaskin.finance.security.util.JwtCore;
import ru.pinyaskin.finance.service.AuthenticationService;

@Component
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtCore jwt;

    @Override
    public AuthenticationResponse signIn(SignInRequest request) {

        return null;
    }

    @Override
    @Transactional
    public AuthenticationResponse signUp(SignUpRequest request) {
        // Создаем User и кладем в бд
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user = userRepository.save(user);

        // Генерируем JWT-токен
        String token = jwt.generateToken(user.getEmail());

        // TODO Аутентифицируем пользователя

        // Формируем ответ
        AuthenticationResponse response = new AuthenticationResponse();
        response.setAccessToken(token);

        return response;
    }
}
