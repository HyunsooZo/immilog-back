package com.backend.immilog.image.domain.model

import com.backend.immilog.image.domain.enums.ImageStatus
import com.backend.immilog.image.domain.enums.ImageType
import com.backend.immilog.image.exception.ImageErrorCode
import com.backend.immilog.image.exception.ImageException

data class Image(
    val seq: Long?,
    val path: String?,
    val imageType: ImageType?,
    val status: ImageStatus?
) {
    companion object {
        fun of(path: String?, imageType: ImageType?): Image {
            if (path?.isBlank() == true) {
                throw ImageException(ImageErrorCode.INVALID_IMAGE_PATH)
            }
            return Image(null, path, imageType, ImageStatus.NORMAL)
        }
    }

    fun delete(): Image = copy(status = ImageStatus.DELETED)
}
