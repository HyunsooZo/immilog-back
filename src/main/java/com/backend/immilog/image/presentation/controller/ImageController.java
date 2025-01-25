package com.backend.immilog.image.presentation.controller;

import com.backend.immilog.image.application.service.ImageService;
import com.backend.immilog.image.domain.enums.ImageType;
import com.backend.immilog.image.presentation.request.ImageRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@Tag(name = "Image API", description = "이미지 업로드 관련 API")
@RequestMapping("/api/v1/images")
@RestController
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping
    @Operation(summary = "이미지 업로드", description = "이미지를 업로드합니다.")
    public ResponseEntity<ImageResponse> uploadImage(
            @RequestParam("multipartFile") List<MultipartFile> multipartFile,
            @RequestParam("imagePath") String imagePath,
            @RequestParam("imageType") ImageType imageType
    ) {
        List<String> data = imageService.saveFiles(multipartFile, imagePath, imageType);

        return ResponseEntity.status(OK).body(ImageResponse.of(data));
    }

    @DeleteMapping
    @Operation(summary = "이미지 삭제", description = "이미지를 삭제합니다.")
    public ResponseEntity<ImageResponse> deleteImage(
            @RequestBody ImageRequest imageRequest
    ) {
        imageService.deleteFile(imageRequest.imagePath());
        return ResponseEntity.status(NO_CONTENT).build();
    }

    @Builder
    public record ImageResponse(
            Integer status,
            String message,
            Object data,
            List<Object> list
    ) {
        public static ImageResponse of(Object data) {
            return ImageResponse.builder()
                    .status(HttpStatus.OK.value())
                    .message(null)
                    .data(data)
                    .list(null)
                    .build();
        }
    }
}