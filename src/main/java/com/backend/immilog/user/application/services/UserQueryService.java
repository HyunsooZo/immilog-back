package com.backend.immilog.user.application.services;

import com.backend.immilog.user.domain.model.user.User;
import com.backend.immilog.user.domain.repositories.UserRepository;
import com.backend.immilog.user.exception.UserErrorCode;
import com.backend.immilog.user.exception.UserException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.backend.immilog.user.exception.UserErrorCode.EXISTING_USER;

@Service
public class UserQueryService {
    private final UserRepository userRepository;

    public UserQueryService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        return userRepository.getByEmail(email)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
    }
    @Transactional(readOnly = true)
    public Boolean isUserExist(String email) {
        return userRepository.getByEmail(email).isPresent();
    }

    @Transactional(readOnly = true)
    public Boolean isNicknameAvailable(String nickname) {
        return userRepository
                .getByUserNickname(nickname)
                .isEmpty();
    }

    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.getById(id).orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
    }


}
