package com.backend.immilog.image.infrastructure.persistence.jpa.entity;

import com.backend.immilog.image.domain.enums.ImageStatus;
import com.backend.immilog.image.domain.enums.ImageType;
import com.backend.immilog.image.domain.model.Image;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
@Entity
@Table(name = "image")
public class ImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private String path;

    @Enumerated(EnumType.STRING)
    private ImageType imageType;

    @Enumerated(EnumType.STRING)
    private ImageStatus status;

    @Builder
    ImageEntity(
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

    public static ImageEntity from(Image image) {
        return ImageEntity.builder()
                .seq(image.getSeq())
                .path(image.getPath())
                .imageType(image.getImageType())
                .status(image.getStatus())
                .build();
    }

    public Image toDomain() {
        return Image.builder()
                .seq(this.seq)
                .path(this.path)
                .imageType(this.imageType)
                .status(this.status)
                .build();
    }
}
