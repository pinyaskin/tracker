package ru.pinyaskin.tracker.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.pinyaskin.tracker.model.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
