package com.backend.immilog.image.application.service.command

import com.backend.immilog.image.domain.model.Image
import com.backend.immilog.image.domain.repository.ImageRepository
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
