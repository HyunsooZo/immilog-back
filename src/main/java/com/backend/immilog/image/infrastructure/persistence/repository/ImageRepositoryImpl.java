package com.backend.immilog.image.infrastructure.persistence.repository;

import com.backend.immilog.image.domain.model.Image;
import com.backend.immilog.image.domain.repository.ImageRepository;
import com.backend.immilog.image.exception.ImageErrorCode;
import com.backend.immilog.image.exception.ImageException;
import com.backend.immilog.image.infrastructure.persistence.jpa.entity.ImageEntity;
import com.backend.immilog.image.infrastructure.persistence.jpa.repository.ImageJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class ImageRepositoryImpl implements ImageRepository {
    private final ImageJpaRepository imageJpaRepository;

    public ImageRepositoryImpl(ImageJpaRepository imageJpaRepository) {
        this.imageJpaRepository = imageJpaRepository;
    }

    @Override
    public Image save(Image image) {
        return imageJpaRepository.save(ImageEntity.from(image)).toDomain();
    }

    @Override
    public Image findByPath(String imagePath) {
        return imageJpaRepository.findByPath(imagePath)
                .orElseThrow(() -> new ImageException(ImageErrorCode.IMAGE_NOT_FOUND))
                .toDomain();
    }
}
