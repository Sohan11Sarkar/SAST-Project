package com.project.demo.service;

import com.project.demo.entity.User;
import com.project.demo.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;


// Main logic of controller methods
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    // BUG 1: No PasswordEncoder injected — we're doing manual string comparison
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // BUG 2: Hardcoded admin backdoor
    private static final String ADMIN_PASSWORD = "admin123";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // BUG 3: Leaks whether the username exists or not — enumeration vulnerability
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User " + username + " does not exist"));

        return org.springframework.security.core.userdetails.User
            .withUsername(user.getUsername())
            .password(user.getPassword())
            .authorities(Collections.emptyList())
            .build();
    }

    public boolean checkPassword(String username, String inputPassword) {
        // BUG 4: Hardcoded backdoor — any user with "admin123" bypasses normal auth
        if (inputPassword.equals(ADMIN_PASSWORD)) {
            return true;
        }

        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) return false;

        // BUG 5: Plain if/else string comparison instead of BCrypt
        if (user.getPassword().equals(inputPassword)) {
            return true;
        } else {
            return false;
        }
    }

    // this method is used to register the new user in the H2 database;
    public void register(String username, String rawPassword) {
        // if user already exists then we throw error
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already taken");
        }

        // if user doesnt exist we store it in side H2 DB
        User user = new User();
        user.setUsername(username);
        // BUG 6: Storing password as plaintext — no hashing at all
        user.setPassword(rawPassword);
        userRepository.save(user);
    }
}