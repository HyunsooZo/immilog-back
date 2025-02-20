package com.backend.immilog.image.infrastructure.persistence.repository

import com.backend.immilog.image.domain.model.Image
import com.backend.immilog.image.domain.repository.ImageRepository
import com.backend.immilog.image.exception.ImageErrorCode
import com.backend.immilog.image.exception.ImageException
import com.backend.immilog.image.infrastructure.persistence.jpa.entity.ImageEntity
import com.backend.immilog.image.infrastructure.persistence.jpa.repository.ImageJpaRepository
import org.springframework.stereotype.Repository

@Repository
class ImageRepositoryImpl(
    private val imageJpaRepository: ImageJpaRepository
) : ImageRepository {
    override fun save(image: Image): Image {
        return ImageEntity.from(image)?.let { imageJpaRepository.save(it).toDomain() } ?: throw ImageException(ImageErrorCode.IMAGE_SAVE_FAILED)
    }

    override fun findByPath(imagePath: String): Image {
        return imageJpaRepository.findByPath(imagePath)
            .orElseThrow { ImageException(ImageErrorCode.IMAGE_NOT_FOUND) }
            .toDomain()
    }
}
