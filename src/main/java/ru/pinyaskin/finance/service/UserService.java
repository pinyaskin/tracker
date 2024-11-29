package ru.pinyaskin.finance.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.pinyaskin.finance.entity.User;
import ru.pinyaskin.finance.repository.UserRepository;
import ru.pinyaskin.finance.security.UserPrincipal;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public UserDetailsService userDetailsService() {
        return this::getUserDetailsByEmail;
    }

    public UserDetails getUserDetailsByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new UserPrincipal(user);
    }
}
