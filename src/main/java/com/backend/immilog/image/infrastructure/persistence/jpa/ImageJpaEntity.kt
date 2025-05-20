package com.backend.immilog.image.infrastructure.persistence.jpa

import com.backend.immilog.image.domain.Image
import com.backend.immilog.image.domain.ImageStatus
import com.backend.immilog.image.domain.ImageType
import jakarta.persistence.*
import org.hibernate.annotations.DynamicUpdate

@DynamicUpdate
@Entity
@Table(name = "image")
open class ImageJpaEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    open var seq: Long? = null,

    @Column(name = "path")
    open var path: String = "",

    @Column(name = "image_type")
    @Enumerated(EnumType.STRING)
    open var imageType: ImageType = ImageType.POST,

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    open var status: ImageStatus = ImageStatus.NORMAL
) {
    companion object {
        fun from(image: Image): ImageJpaEntity =
            ImageJpaEntity(
                seq = image.seq,
                path = image.path,
                imageType = image.imageType,
                status = image.status
            )
    }

    fun toDomain(): Image = Image(
        seq = this.seq,
        path = this.path,
        imageType = this.imageType,
        status = this.status
    )
}
