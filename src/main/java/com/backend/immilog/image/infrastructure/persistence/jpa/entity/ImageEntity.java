package com.backend.immilog.image.infrastructure.persistence.jpa.entity;

import com.backend.immilog.image.domain.enums.ImageStatus;
import com.backend.immilog.image.domain.enums.ImageType;
import com.backend.immilog.image.domain.model.Image;
import jakarta.persistence.*;
import org.hibernate.annotations.DynamicUpdate;

@DynamicUpdate
@Entity
@Table(name = "image")
public class ImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long seq;

    @Column(name = "path")
    private String path;

    @Column(name = "image_type")
    @Enumerated(EnumType.STRING)
    private ImageType imageType;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ImageStatus status;

    protected ImageEntity() {}

    protected ImageEntity(
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
        return new ImageEntity(
                image.seq(),
                image.path(),
                image.imageType(),
                image.status()
        );
    }

    public Image toDomain() {
        return new Image(
                this.seq,
                this.path,
                this.imageType,
                this.status
        );
    }
}
