package com.backend.immilog.image

import com.amazonaws.services.s3.AmazonS3
import com.backend.immilog.global.exception.CommonErrorCode.IMAGE_UPLOAD_FAILED
import com.backend.immilog.global.exception.CustomException
import com.backend.immilog.image.infrastructure.gateway.S3FileStorageHandler
import com.backend.immilog.notification.application.service.DiscordSendingService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.ArgumentMatchers.anyString
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mockito.*
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.io.InputStream
import java.net.URI
import java.net.URISyntaxException

@DisplayName("S3FileStorageHandler 클래스 테스트")
class S3FileStorageHandlerTest {

    private val amazonS3: AmazonS3 = mock(AmazonS3::class.java)
    private val discordSendingService: DiscordSendingService = mock(DiscordSendingService::class.java)
    private val multipartFile: MultipartFile = mock(MultipartFile::class.java)
    private val s3FileStorageHandler = S3FileStorageHandler(amazonS3, discordSendingService).apply {
        val field = this::class.java.getDeclaredField("bucket")
        field.isAccessible = true
        field.set(this, "test-bucket")
    }

    @Test
    @DisplayName("파일 업로드가 성공적으로 수행되는지 테스트")
    fun uploadFileSuccessfully() {
        val imagePath = "path/to/image"
        val expectedUrl = "http://example.com/path/to/image"

        `when`(multipartFile.originalFilename).thenReturn("file.png")
        `when`(multipartFile.size).thenReturn(100L)
        `when`(multipartFile.contentType).thenReturn("image/png")
        `when`(multipartFile.inputStream).thenReturn(mock(InputStream::class.java))
        try {
            `when`(amazonS3.getUrl(anyString(), anyString())).thenReturn(URI(expectedUrl).toURL())
        } catch (e: URISyntaxException) {
            throw RuntimeException(e)
        }

        val actualUrl = s3FileStorageHandler.uploadFile(multipartFile, imagePath)
        assertEquals(expectedUrl, actualUrl)
    }

    @Test
    @DisplayName("파일 삭제가 성공적으로 수행되는지 테스트")
    fun deleteFileSuccessfully() {
        val imagePath = "path/to/image"
        doNothing().`when`(amazonS3).deleteObject(anyString(), anyString())

        s3FileStorageHandler.deleteFile(imagePath)

        verify(amazonS3, times(1)).deleteObject(anyString(), eq(imagePath))
    }

    @Test
    @DisplayName("파일 삭제 시 경로가 null인 경우 예외가 발생하지 않는지 테스트")
    fun deleteFileDoesNotThrowExceptionWhenPathIsNull() {
        s3FileStorageHandler.deleteFile(null)
        verify(amazonS3, never()).deleteObject(anyString(), anyString())
    }

    @Test
    @DisplayName("파일 업로드 시 IOException이 발생하는 경우 CustomException이 발생하는지 테스트")
    fun uploadFileThrowsCustomExceptionOnIOException() {
        val imagePath = "path/to/image"

        `when`(multipartFile.originalFilename).thenReturn("file.png")
        `when`(multipartFile.size).thenReturn(100L)
        `when`(multipartFile.contentType).thenReturn("image/png")
        `when`(multipartFile.inputStream).thenThrow(IOException::class.java)

        val exception = assertThrows<CustomException> {
            s3FileStorageHandler.uploadFile(multipartFile, imagePath)
        }
        assertEquals(IMAGE_UPLOAD_FAILED, exception.errorCode)
    }
}
