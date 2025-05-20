package com.backend.immilog.image.domain

interface ImageRepository {
    fun save(image: Image): Image
    fun findByPath(imagePath: String): Image
}
