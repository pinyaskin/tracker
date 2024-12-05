package ru.pinyaskin.tracker.exception.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.pinyaskin.tracker.exception.SignUpException;

@ControllerAdvice
public class ExceptionHandlingController {
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT,
            reason = "Data integrity violation")
    public void dataIntegrity() {

    }

    @ExceptionHandler(SignUpException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    @ResponseBody String handleSignUpException(SignUpException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ResponseBody String invalidCredentials(BadCredentialsException ex) {
        if (ex.getMessage().contains("Invalid Credentials") || ex.getMessage().contains("Bad credentials")) {
            return "Неверный Email или пароль";
        }
        return ex.getMessage();
    }
}
