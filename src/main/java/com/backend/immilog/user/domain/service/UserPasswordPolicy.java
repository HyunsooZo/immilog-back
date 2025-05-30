package com.backend.immilog.user.domain.service;

import com.backend.immilog.user.exception.UserErrorCode;
import com.backend.immilog.user.exception.UserException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserPasswordPolicy {

    private final PasswordEncoder passwordEncoder;

    public UserPasswordPolicy(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public void validate(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new UserException(UserErrorCode.PASSWORD_NOT_MATCH);
        }
    }

    public String encode(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new UserException(UserErrorCode.PASSWORD_NOT_MATCH);
        }
        return passwordEncoder.encode(password);
    }
}
