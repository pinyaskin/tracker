package ru.pinyaskin.tracker.service;


import ru.pinyaskin.tracker.model.entity.User;

public interface UserService {
    User getUserByEmail(String email);
}
