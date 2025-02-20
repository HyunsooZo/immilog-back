package com.backend.immilog.image.infrastructure.persistence.jpa.repository

import com.backend.immilog.image.infrastructure.persistence.jpa.entity.ImageEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ImageJpaRepository : JpaRepository<ImageEntity, Long> {
    fun findByPath(path: String): Optional<ImageEntity>
}
