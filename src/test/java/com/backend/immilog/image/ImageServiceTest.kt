package com.backend.immilog.image

import com.backend.immilog.image.domain.enums.ImageType
import com.backend.immilog.image.application.service.ImageService
import com.backend.immilog.image.application.service.command.ImageCommandService
import com.backend.immilog.image.application.service.query.ImageQueryService
import com.backend.immilog.image.infrastructure.gateway.FileStorageHandler
import com.backend.immilog.image.domain.model.Image
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.springframework.mock.web.MockMultipartFile
import org.springframework.web.multipart.MultipartFile

@DisplayName("이미지 서비스 테스트")
class ImageServiceTest {
    private val fileStorageHandler: FileStorageHandler = mock(FileStorageHandler::class.java)
    private val imageCommandService: ImageCommandService = mock(ImageCommandService::class.java)
    private val imageQueryService: ImageQueryService = mock(ImageQueryService::class.java)
    private val imageService = ImageService(fileStorageHandler, imageCommandService, imageQueryService)

    @Test
    @DisplayName("이미지 업로드")
    fun uploadImage() {
        // given
        val dummyFile = MockMultipartFile("file", "test.jpg", "image/jpeg", "test".toByteArray())
        val files: List<MultipartFile> = listOf(dummyFile)
        val imagePath = "imagePath"
        val mockUrl = "https://example.com/path"

        `when`(fileStorageHandler.uploadFile(dummyFile, imagePath)).thenReturn(mockUrl)

        // when
        val images = imageService.saveFiles(files, imagePath, ImageType.POST)

        // then
        assertThat(images).isNotEmpty
        assertThat(images[0]).isEqualTo(mockUrl)
        verify(fileStorageHandler, times(1)).uploadFile(dummyFile, imagePath)
    }

    @Test
    @DisplayName("이미지 삭제")
    fun deleteImage() {
        // given
        val imagePath = "https://imagePath"
        `when`(imageQueryService.getImageByPath(imagePath)).thenReturn(Image.of(imagePath, ImageType.POST))
        // when
        imageService.deleteFile(imagePath)
        // then
        verify(fileStorageHandler, times(1)).deleteFile(eq(imagePath))
    }
}
