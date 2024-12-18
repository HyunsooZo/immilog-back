package com.backend.immilog.user.application.services.query;

import com.backend.immilog.user.domain.model.user.User;
import com.backend.immilog.user.domain.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserQueryService {
    private final UserRepository userRepository;

    public UserQueryService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserByEmail(String email) {
        return userRepository.getByEmail(email);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserByNickname(String nickname) {
        return userRepository.getByUserNickname(nickname);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserById(Long id) {
        return userRepository.getById(id);
    }

}
