package com.backend.immilog.image.domain

import com.backend.immilog.image.exception.ImageErrorCode
import com.backend.immilog.image.exception.ImageException

data class Image(
    val seq: Long?,
    val path: String,
    val imageType: ImageType,
    val status: ImageStatus
) {
    companion object {
        fun of(path: String, imageType: ImageType): Image {
            require(path.isNotBlank()) { throw ImageException(ImageErrorCode.INVALID_IMAGE_PATH) }
            return Image(null, path, imageType, ImageStatus.NORMAL)
        }
    }

    fun delete(): Image = copy(status = ImageStatus.DELETED)
}

enum class ImageStatus {
    NORMAL,
    DELETED
}

enum class ImageType {
    PROFILE,
    POST,
    JOB_POST
}
