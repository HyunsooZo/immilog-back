package com.backend.immilog.image.domain.model;

import com.backend.immilog.image.domain.enums.ImageStatus;
import com.backend.immilog.image.domain.enums.ImageType;
import com.backend.immilog.image.exception.ImageErrorCode;
import com.backend.immilog.image.exception.ImageException;
import lombok.Builder;
import lombok.Getter;

import java.util.Optional;

@Getter
public class Image {
    private Long seq;
    private String path;
    private ImageType imageType;
    private ImageStatus status;

    @Builder
    Image(
            Long seq,
            String path,
            ImageType imageType,
            ImageStatus status
    ) {
        this.seq = seq;
        this.path = path;
        this.imageType = imageType;
        this.status = status;
    }

    public Image(
            String path,
            ImageType imageType
    ) {
        setPath(path);
        this.imageType = imageType;
        this.status = ImageStatus.NORMAL;
    }

    public void delete() {
        this.status = ImageStatus.DELETED;
    }

    void setPath(String path) {
        Optional.ofNullable(path)
                .filter(p -> p.startsWith("https://"))
                .ifPresentOrElse(
                        p -> this.path = p,
                        () -> {
                            throw new ImageException(ImageErrorCode.INVALID_IMAGE_PATH);
                        }
                );
    }

    void setImageType(ImageType imageType) {
        Optional.ofNullable(imageType)
                .ifPresentOrElse(
                        it -> this.imageType = it,
                        () -> {
                            throw new ImageException(ImageErrorCode.INVALID_IMAGE_TYPE);
                        }
                );
    }
}
