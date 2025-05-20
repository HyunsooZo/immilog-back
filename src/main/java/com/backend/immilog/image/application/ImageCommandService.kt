package com.backend.immilog.image.application

import com.backend.immilog.image.domain.Image
import com.backend.immilog.image.domain.ImageRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ImageCommandService(
    private val imageRepository: ImageRepository
) {
    @Transactional
    fun save(image: Image) {
        imageRepository.save(image)
    }
}
