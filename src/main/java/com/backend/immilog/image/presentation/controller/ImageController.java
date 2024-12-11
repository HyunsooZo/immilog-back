package com.backend.immilog.image.presentation.controller;

import com.backend.immilog.global.model.GlobalApiResponse;
import com.backend.immilog.image.application.service.ImageService;
import com.backend.immilog.image.presentation.request.ImageRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@Tag(name = "Image API", description = "이미지 업로드 관련 API")
@RequestMapping("/api/v1/images")
@RequiredArgsConstructor
@RestController
public class ImageController {

    private final ImageService imageService;

    @PostMapping
    @Operation(summary = "이미지 업로드", description = "이미지를 업로드합니다.")
    public ResponseEntity<GlobalApiResponse> uploadImage(
            List<MultipartFile> multipartFile,
            @RequestParam("imagePath") String imagePath
    ) {
        List<String> data = imageService.saveFiles(multipartFile, imagePath);

        return ResponseEntity
                .status(OK)
                .body(GlobalApiResponse.of(data));
    }

    @DeleteMapping
    @Operation(summary = "이미지 삭제", description = "이미지를 삭제합니다.")
    public ResponseEntity<GlobalApiResponse> deleteImage(
            ImageRequest imageRequest
    ) {
        imageService.deleteFile(imageRequest.imagePath());
        return ResponseEntity.status(NO_CONTENT).build();
    }
}