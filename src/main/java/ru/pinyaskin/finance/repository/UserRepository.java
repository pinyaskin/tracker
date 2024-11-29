package ru.pinyaskin.finance.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.pinyaskin.finance.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
