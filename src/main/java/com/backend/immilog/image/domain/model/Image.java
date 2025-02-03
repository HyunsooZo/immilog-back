package com.backend.immilog.image.domain.model;

import com.backend.immilog.image.domain.enums.ImageStatus;
import com.backend.immilog.image.domain.enums.ImageType;
import com.backend.immilog.image.exception.ImageErrorCode;
import com.backend.immilog.image.exception.ImageException;

public record Image(
        Long seq,
        String path,
        ImageType imageType,
        ImageStatus status
) {
    public static Image of(
            String path,
            ImageType imageType
    ) {
        if (path == null || path.trim().isBlank() || imageType == null) {
            throw new ImageException(ImageErrorCode.INVALID_IMAGE_PATH);
        }
        return new Image(null, path, imageType, ImageStatus.NORMAL);
    }

    public Image delete() {
        return new Image(
                this.seq,
                this.path,
                this.imageType,
                ImageStatus.DELETED
        );
    }
}
