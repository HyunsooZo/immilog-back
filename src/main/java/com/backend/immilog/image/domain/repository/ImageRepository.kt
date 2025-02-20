package com.backend.immilog.image.domain.repository

import com.backend.immilog.image.domain.model.Image

interface ImageRepository {
    fun save(image: Image): Image
    fun findByPath(imagePath: String): Image
}
