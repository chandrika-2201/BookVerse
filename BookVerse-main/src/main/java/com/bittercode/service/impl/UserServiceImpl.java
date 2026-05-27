package com.bittercode.service.impl;

import com.bittercode.model.User;
import com.bittercode.model.UserRole;
import com.bittercode.repository.UserRepository;
import com.bittercode.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.lang.Nullable;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Nullable
    public User login(UserRole role, String username, String password) {
        return userRepository.findByEmailIdAndPasswordAndRole(username, password, role).orElse(null);
    }

    @Override
    public String register(UserRole role, User user) {
        try {
            user.setRole(role);
            user.setEmailId(user.getMailId());
            if (userRepository.existsById(user.getEmailId())) {
                return "User already registered with this email !!";
            }
            userRepository.save(user);
            return "SUCCESS";
        } catch (Exception e) {
            return "FAILURE: " + e.getMessage();
        }
    }
}
