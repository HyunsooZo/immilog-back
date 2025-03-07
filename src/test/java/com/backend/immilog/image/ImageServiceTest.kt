package com.backend.immilog.image

import com.backend.immilog.image.application.service.ImageService
import com.backend.immilog.image.application.service.command.ImageCommandService
import com.backend.immilog.image.application.service.query.ImageQueryService
import com.backend.immilog.image.domain.enums.ImageType
import com.backend.immilog.image.infrastructure.gateway.FileStorageHandler
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.mock.web.MockMultipartFile
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

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
        val imagePath = "/imagePath"
        val mockUrl = "https://example.com/imagePath"

        mockStatic(ServletUriComponentsBuilder::class.java).use { mockedStatic ->
            val mockBuilder = mock(ServletUriComponentsBuilder::class.java)
            val mockUriComponents = mock(org.springframework.web.util.UriComponents::class.java)
            `when`(ServletUriComponentsBuilder.fromCurrentContextPath()).thenReturn(mockBuilder)
            // anyString()을 사용해서 imagePath가 무엇이든 mockBuilder를 반환하도록 스텁 처리
            `when`(mockBuilder.path(anyString())).thenReturn(mockBuilder)
            `when`(mockBuilder.build()).thenReturn(mockUriComponents)
            `when`(mockUriComponents.toUriString()).thenReturn(mockUrl)
            `when`(fileStorageHandler.uploadFile(dummyFile, imagePath)).thenReturn(mockUrl)

            // when
            val images = imageService.saveFiles(files, imagePath, ImageType.POST)

            // then
            assertThat(images).isNotEmpty
            assertThat(images[0]).isEqualTo(mockUrl)
            verify(fileStorageHandler, times(1)).uploadFile(dummyFile, imagePath)
        }
    }
}

