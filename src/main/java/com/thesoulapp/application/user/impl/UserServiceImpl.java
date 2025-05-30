package com.thesoulapp.application.user.impl;

import com.thesoulapp.application.user.UserService;
import com.thesoulapp.domain.user.User;
import com.thesoulapp.domain.user.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Optional;
import com.thesoulapp.infrastructure.security.JwtProvider;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final JwtProvider jwtProvider;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public User register(String email, String password, User.Role role, String profileJson) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email already registered");
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        user.setProfile(profileJson);
        return userRepository.save(user);
    }

    @Override
    public String login(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty() || !passwordEncoder.matches(password, userOpt.get().getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        return jwtProvider.generateToken(userOpt.get());
    }

    @Override
    public User getProfile(String email) {
        return userRepository.findByEmail(email).orElseThrow();
    }
} 