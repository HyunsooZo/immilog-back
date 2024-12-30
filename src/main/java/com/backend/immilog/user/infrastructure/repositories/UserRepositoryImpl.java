package com.backend.immilog.user.infrastructure.repositories;

import com.backend.immilog.user.domain.model.user.User;
import com.backend.immilog.user.domain.repositories.UserRepository;
import com.backend.immilog.user.infrastructure.jpa.entity.UserEntity;
import com.backend.immilog.user.infrastructure.jpa.repositories.UserJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final UserJpaRepository userJpaRepository;

    public UserRepositoryImpl(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public Optional<User> getByEmail(
            String email
    ) {
        return userJpaRepository
                .findByEmail(email)
                .map(UserEntity::toDomain);
    }

    @Override
    public Optional<User> getByUserNickname(
            String nickname
    ) {
        return userJpaRepository
                .findByNickname(nickname)
                .map(UserEntity::toDomain);
    }

    @Override
    public Optional<User> getById(
            Long id
    ) {
        return userJpaRepository
                .findById(id)
                .map(UserEntity::toDomain);
    }

    @Override
    public User save(
            User of
    ) {
        return userJpaRepository.save(UserEntity.from(of)).toDomain();
    }
}
