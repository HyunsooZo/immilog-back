//package com.backend.immilog.image
//
//import com.backend.immilog.image.application.ImageCommandService
//import com.backend.immilog.image.application.ImageQueryService
//import com.backend.immilog.image.application.ImageUploadUseCase
//import com.backend.immilog.image.domain.ImageType
//import com.backend.immilog.image.infrastructure.gateway.FileStorageHandler
//import org.assertj.core.api.Assertions.assertThat
//import org.junit.jupiter.api.DisplayName
//import org.junit.jupiter.api.Test
//import org.mockito.Mockito.*
//import org.springframework.mock.web.MockMultipartFile
//import org.springframework.web.multipart.MultipartFile
//
//@DisplayName("이미지 서비스 테스트")
//class ImageUploaderTest {
//    private val fileStorageHandler: FileStorageHandler = mock(FileStorageHandler::class.java)
//    private val imageCommandService: ImageCommandService = mock(ImageCommandService::class.java)
//    private val imageQueryService: ImageQueryService = mock(ImageQueryService::class.java)
//    private val imageUploader: ImageUploadUseCase =
//        ImageUploadUseCase.ImageUploader(fileStorageHandler, imageCommandService, imageQueryService)
//
//    @Test
//    @DisplayName("이미지 업로드")
//    fun uploadImage() {
//        // given
//        val dummyFile = MockMultipartFile("file", "test.jpg", "image/jpeg", "test".toByteArray())
//        val files: List<MultipartFile> = listOf(dummyFile)
//        val imagePath = "/imagePath"
//        val mockUrl = "https://example.com/imagePath"
//            // when
//            val images = imageUploader.saveFiles(files, imagePath, ImageType.POST)
//            // then
//            assertThat(images).isNotEmpty
//            assertThat(images[0]).isEqualTo(mockUrl)
//            verify(fileStorageHandler, times(1)).uploadFile(dummyFile, imagePath)
//        }
//
//}
//
