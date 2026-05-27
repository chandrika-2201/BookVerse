package com.bittercode.service;

import com.bittercode.model.User;
import com.bittercode.model.UserRole;
import org.springframework.lang.Nullable;

public interface UserService {
    @Nullable
    User login(UserRole role, String username, String password);
    String register(UserRole role, User user);
}
