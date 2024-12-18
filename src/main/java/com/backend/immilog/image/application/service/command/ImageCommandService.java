package com.backend.immilog.image.application.service.command;

import com.backend.immilog.image.domain.model.Image;
import com.backend.immilog.image.domain.repository.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ImageCommandService {
    private final ImageRepository imageRepository;

    public ImageCommandService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Transactional
    public void save(Image image) {
        imageRepository.save(image);
    }
}
