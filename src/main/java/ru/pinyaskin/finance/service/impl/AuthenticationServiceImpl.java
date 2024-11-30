package ru.pinyaskin.finance.service.impl;

import org.springframework.transaction.annotation.Transactional;
import ru.pinyaskin.finance.model.payload.request.SignInRequest;
import ru.pinyaskin.finance.model.payload.request.SignUpRequest;
import ru.pinyaskin.finance.model.payload.response.AuthenticationResponse;
import ru.pinyaskin.finance.service.AuthenticationService;

public class AuthenticationServiceImpl implements AuthenticationService {

    @Override
    public AuthenticationResponse signIn(SignInRequest request) {

        return null;
    }

    @Override
    @Transactional
    public AuthenticationResponse signUp(SignUpRequest request) {


        AuthenticationResponse response = new AuthenticationResponse();


        return null;
    }
}
