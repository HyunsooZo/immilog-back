package com.backend.immilog.image.domain.repository;

import com.backend.immilog.image.domain.model.Image;

public interface ImageRepository {
    Image save(Image image);

    Image findByPath(String imagePath);
}
