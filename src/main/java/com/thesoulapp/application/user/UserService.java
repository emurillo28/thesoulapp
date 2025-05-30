package com.thesoulapp.application.user;

import com.thesoulapp.domain.user.User;

public interface UserService {
    User register(String email, String password, User.Role role, String profileJson);
    String login(String email, String password);
    User getProfile(String email);
} 