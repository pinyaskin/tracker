package ru.pinyaskin.finance.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.pinyaskin.finance.model.entity.User;
import ru.pinyaskin.finance.repository.UserRepository;
import ru.pinyaskin.finance.security.UserPrincipal;
import ru.pinyaskin.finance.service.UserService;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDetails getUserDetailsByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new UserPrincipal(user);
    }
}
