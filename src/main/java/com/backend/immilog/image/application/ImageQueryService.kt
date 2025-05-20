package com.backend.immilog.image.application

import com.backend.immilog.image.domain.Image
import com.backend.immilog.image.domain.ImageRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ImageQueryService(
    private val imageRepository: ImageRepository
) {
    @Transactional(readOnly = true)
    fun getImageByPath(imagePath: String): Image = imageRepository.findByPath(imagePath)
}
