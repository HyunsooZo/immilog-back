package com.backend.immilog.user.infrastructure.jpa.repositories;

import com.backend.immilog.user.infrastructure.jpa.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByAuth_Email(String email);

    Optional<UserEntity> findByProfile_Nickname(String nickname);
}
