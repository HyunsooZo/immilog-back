package com.backend.immilog.image.application.service.command;

import com.backend.immilog.image.domain.model.Image;
import com.backend.immilog.image.domain.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ImageCommandService {
    private final ImageRepository imageRepository;

    @Transactional
    public void save(Image image) {
        imageRepository.save(image);
    }
}
