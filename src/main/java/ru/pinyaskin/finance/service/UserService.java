package ru.pinyaskin.finance.service;


import ru.pinyaskin.finance.model.entity.User;

public interface UserService {
    User getUserByEmail(String email);
}
