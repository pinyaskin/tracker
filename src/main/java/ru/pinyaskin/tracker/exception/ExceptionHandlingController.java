package ru.pinyaskin.tracker.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

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
}
