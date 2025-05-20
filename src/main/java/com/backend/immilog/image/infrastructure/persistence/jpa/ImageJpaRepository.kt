package com.backend.immilog.image.infrastructure.persistence.jpa

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ImageJpaRepository : JpaRepository<ImageJpaEntity, Long> {
    fun findByPath(path: String): Optional<ImageJpaEntity>
}
