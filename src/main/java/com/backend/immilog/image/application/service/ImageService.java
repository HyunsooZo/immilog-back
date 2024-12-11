package com.backend.immilog.image.application.service;

import com.backend.immilog.image.infrastructure.gateway.FileStorageHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final FileStorageHandler fileStorageHandler;

    public List<String> saveFiles(
            List<MultipartFile> multipartFiles,
            String imagePath
    ) {
        return multipartFiles.stream()
                .map(multipartFile ->
                        fileStorageHandler.uploadFile(multipartFile, imagePath)
                )
                .toList();
    }

    public void deleteFile(String imagePath) {
        fileStorageHandler.deleteFile(imagePath);
    }
}
