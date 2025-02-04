package com.backend.immilog.global.application;

import com.backend.immilog.image.application.service.ImageService;
import com.backend.immilog.image.application.service.command.ImageCommandService;
import com.backend.immilog.image.application.service.query.ImageQueryService;
import com.backend.immilog.image.domain.enums.ImageType;
import com.backend.immilog.image.domain.model.Image;
import com.backend.immilog.image.infrastructure.gateway.FileStorageHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DisplayName("이미지 서비스 테스트")
class ImageServiceTest {
    private final FileStorageHandler fileStorageHandler = mock(FileStorageHandler.class);
    private final ImageCommandService imageCommandService = mock(ImageCommandService.class);
    private final ImageQueryService imageQueryService = mock(ImageQueryService.class);
    private final ImageService imageService = new ImageService(
            fileStorageHandler,
            imageCommandService,
            imageQueryService
    );

    @Test
    @DisplayName("이미지 업로드")
    void uploadImage() {
        // given
        List<MultipartFile> files = List.of(mock(MultipartFile.class));
        String imagePath = "imagePath";
        String mockUrl = "https://example.com/path";

        when(fileStorageHandler.uploadFile(any(MultipartFile.class), eq(imagePath)))
                .thenReturn(mockUrl);

        // when
        List<String> images = imageService.saveFiles(files, imagePath, ImageType.POST);

        // then
        assertThat(images).isNotEmpty();
        assertThat(images.get(0)).isEqualTo(mockUrl);

        verify(fileStorageHandler, times(1)).uploadFile(any(MultipartFile.class), eq(imagePath));
    }

    @Test
    @DisplayName("이미지 삭제")
    void deleteImage() {
        // given
        String imagePath = "https://imagePath";
        when(imageQueryService.getImageByPath(imagePath)).thenReturn(Image.of(imagePath, ImageType.POST));
        // when
        imageService.deleteFile(imagePath);
        // then
        verify(fileStorageHandler, times(1)).deleteFile(eq(imagePath));
    }
}
