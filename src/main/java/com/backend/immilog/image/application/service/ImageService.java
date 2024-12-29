package com.backend.immilog.image.application.service;

import com.backend.immilog.image.application.service.command.ImageCommandService;
import com.backend.immilog.image.application.service.query.ImageQueryService;
import com.backend.immilog.image.domain.enums.ImageType;
import com.backend.immilog.image.domain.model.Image;
import com.backend.immilog.image.infrastructure.gateway.FileStorageHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class ImageService {
    private final FileStorageHandler fileStorageHandler;
    private final ImageCommandService imageCommandService;
    private final ImageQueryService imageQueryService;

    public ImageService(
            FileStorageHandler fileStorageHandler,
            ImageCommandService imageCommandService,
            ImageQueryService imageQueryService
    ) {
        this.fileStorageHandler = fileStorageHandler;
        this.imageCommandService = imageCommandService;
        this.imageQueryService = imageQueryService;
    }

    public List<String> saveFiles(
            List<MultipartFile> files,
            String imagePath,
            ImageType imageType
    ) {
        return files.stream()
                .map(file -> {
                    String url = fileStorageHandler.uploadFile(file, imagePath);
                    imageCommandService.save(new Image(url, imageType));
                    return url;
                })
                .toList();
    }

    public void deleteFile(String imagePath) {
        Optional.ofNullable(imagePath)
                .filter(image -> !image.isBlank())
                .ifPresent(path -> {
                    fileStorageHandler.deleteFile(path);
                    Image image = imageQueryService.getImageByPath(path);
                    image.delete();
                    imageCommandService.save(image);
                });
    }
}
