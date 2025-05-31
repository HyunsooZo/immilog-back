package com.backend.immilog.image

import com.backend.immilog.image.application.ImageUploadUseCase
import com.backend.immilog.image.domain.ImageType
import com.backend.immilog.image.presentation.ImageController
import com.backend.immilog.image.presentation.ImageRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.http.HttpStatus.OK
import org.springframework.web.multipart.MultipartFile

@DisplayName("이미지 컨트롤러 테스트")
class ImageControllerTest {
    private val imageUploader: ImageUploadUseCase = mock(ImageUploadUseCase.ImageUploader::class.java)
    private val imageController = ImageController(imageUploader)

    @Test
    @DisplayName("이미지 업로드")
    fun uploadImage() {
        // given
        val files: List<MultipartFile> = listOf(mock(MultipartFile::class.java))
        val imagePath = "imagePath"
        val imageDTO: List<String> = listOf("imageUrl")
        `when`(imageUploader.saveFiles(files, imagePath, ImageType.POST)).thenReturn(imageDTO)
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
        verify(imageUploader, times(1)).deleteFile(imagePath,"")
        assertThat(response.statusCode).isEqualTo(NO_CONTENT)
    }
}
