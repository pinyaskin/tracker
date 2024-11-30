package ru.pinyaskin.finance.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {
    UserDetails getUserDetailsByEmail(String email);
}
