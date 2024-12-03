package ru.pinyaskin.finance.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.pinyaskin.finance.model.payload.request.SignInRequest;
import ru.pinyaskin.finance.model.payload.request.SignUpRequest;
import ru.pinyaskin.finance.model.payload.response.AuthenticationResponse;
import ru.pinyaskin.finance.service.AuthenticationService;

/**
 * Контроллер аутентификации.
 * Предоставляет endpoints для аутентификации пользователей
 * @author Pinyaskin Iliya
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/sign-in")
    public ResponseEntity<AuthenticationResponse> signIn(@RequestBody SignInRequest request) {
        AuthenticationResponse response = authenticationService.signIn(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<AuthenticationResponse> signUp(@Valid @RequestBody SignUpRequest request) {
        AuthenticationResponse response = authenticationService.signUp(request);
        return ResponseEntity.ok(response);
    }
}
