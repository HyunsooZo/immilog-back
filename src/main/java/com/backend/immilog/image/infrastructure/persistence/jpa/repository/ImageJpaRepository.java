package com.backend.immilog.image.infrastructure.persistence.jpa.repository;

import com.backend.immilog.image.infrastructure.persistence.jpa.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageJpaRepository extends JpaRepository<ImageEntity, Long> {
    Optional<ImageEntity> findByPath(String path);
}
