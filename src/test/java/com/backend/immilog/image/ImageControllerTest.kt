package com.backend.immilog.image

import com.backend.immilog.image.application.service.ImageService
import com.backend.immilog.image.domain.enums.ImageType
import com.backend.immilog.image.presentation.controller.ImageController
import com.backend.immilog.image.presentation.payload.ImageRequest
import com.backend.immilog.image.presentation.payload.ImageResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mockito.*
import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.web.multipart.MultipartFile

@DisplayName("이미지 컨트롤러 테스트")
class ImageControllerTest {
    private val imageService: ImageService = mock(ImageService::class.java)
    private val imageController = ImageController(imageService)

    @Test
    @DisplayName("이미지 업로드")
    fun uploadImage() {
        // given
        val files: List<MultipartFile> = listOf(mock(MultipartFile::class.java))
        val imagePath = "imagePath"
        val imageDTO: List<String> = listOf("imageUrl")
        `when`(imageService.saveFiles(files, imagePath, ImageType.POST)).thenReturn(imageDTO)
        // when
        val response = imageController.uploadImage(files, imagePath, ImageType.POST)
        // then
        assertThat(response.statusCode).isEqualTo(OK)
        val data: List<String> = response.body?.data ?: emptyList()
        assertThat(data.first()).isEqualTo("imageUrl")
    }

    @Test
    @DisplayName("이미지 삭제")
    fun deleteImage() {
        // given
        val imagePath = "imagePath"
        val param = ImageRequest("directory", imagePath)
        // when
        val response = imageController.deleteImage(param)
        // then
        verify(imageService, times(1)).deleteFile(imagePath)
        assertThat(response.statusCode).isEqualTo(NO_CONTENT)
    }

    @Test
    @DisplayName("이미지 업로드 - 파일 없음")
    fun uploadImage_noFiles() {
        // given
        val files: List<MultipartFile> = emptyList()
        val imagePath = "imagePath"
        `when`(imageService.saveFiles(files, imagePath, ImageType.POST)).thenReturn(emptyList())
        // when
        val response = imageController.uploadImage(files, imagePath, ImageType.POST)
        // then
        assertThat(response.statusCode).isEqualTo(OK)
        val data: List<String> = response.body?.data ?: emptyList()
        assertThat(data).isEmpty()
    }

    @Test
    @DisplayName("이미지 삭제 - 경로 없음")
    fun deleteImage_noPath() {
        // given
        val imagePath = ""
        val param = ImageRequest("directory", imagePath)
        // when
        val response = imageController.deleteImage(param)
        // then
        verify(imageService, times(0)).deleteFile(imagePath)
        assertThat(response.statusCode).isEqualTo(NO_CONTENT)
    }
}
