package ru.pinyaskin.finance.service;

import org.springframework.stereotype.Service;
import ru.pinyaskin.finance.model.payload.request.SignInRequest;
import ru.pinyaskin.finance.model.payload.request.SignUpRequest;
import ru.pinyaskin.finance.model.payload.response.AuthenticationResponse;

/**
 * Сервис аутентификации пользователей
 * Предоставляет интерфейс для регистрации и входа пользователя.
 * @author Pinyaskin Ilya
 */
@Service
public interface AuthenticationService {
    /**
     * Метод входа в сервис.
     * Аутентифицирует пользователя на основе предоставленных данных.
     * @param request тело с email и паролем для входа
     * @return JWT-токены доступа
     */
    AuthenticationResponse signIn(SignInRequest request);

    /**
     * Метод регистрации.
     * Создает нового пользователя в базе данных, генерирует JWT-токены
     * @param request запрос на регистрацию
     * @return JWT-токены доступа
     */
    AuthenticationResponse signUp(SignUpRequest request);
}
