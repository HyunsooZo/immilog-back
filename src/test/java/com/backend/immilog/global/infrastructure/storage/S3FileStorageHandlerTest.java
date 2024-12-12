package com.backend.immilog.global.infrastructure.storage;

import com.amazonaws.services.s3.AmazonS3;
import com.backend.immilog.global.exception.CustomException;
import com.backend.immilog.image.infrastructure.gateway.S3FileStorageHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;

import static com.backend.immilog.global.exception.CommonErrorCode.IMAGE_UPLOAD_FAILED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@DisplayName("S3FileStorageHandler 클래스 테스트")
class S3FileStorageHandlerTest {

    private final AmazonS3 amazonS3 = mock(AmazonS3.class);
    private final MultipartFile multipartFile = mock(MultipartFile.class);
    private final S3FileStorageHandler s3FileStorageHandler = new S3FileStorageHandler(amazonS3);

    @BeforeEach
    void setUp() throws Exception {
        setFieldValue(s3FileStorageHandler);
    }

    private <T> void setFieldValue(
            Object target
    ) throws Exception {
        Field field = target.getClass().getDeclaredField("bucket");
        field.setAccessible(true);
        field.set(target, "test-bucket");
    }

    @Test
    @DisplayName("파일 업로드가 성공적으로 수행되는지 테스트")
    void uploadFileSuccessfully() throws IOException {
        String imagePath = "path/to/image";
        String expectedUrl = "http://example.com/path/to/image";
        String originalFileName = "path/to/image/1234567890abcdef.png";

        when(multipartFile.getOriginalFilename()).thenReturn("file.png");
        when(multipartFile.getSize()).thenReturn(100L);
        when(multipartFile.getContentType()).thenReturn("image/png");
        when(multipartFile.getInputStream()).thenReturn(mock(InputStream.class));
        try {
            when(amazonS3.getUrl(anyString(), anyString())).thenReturn(new URI(expectedUrl).toURL());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        String actualUrl = s3FileStorageHandler.uploadFile(multipartFile, imagePath);

        assertEquals(expectedUrl, actualUrl);
    }

    @Test
    @DisplayName("파일 삭제가 성공적으로 수행되는지 테스트")
    void deleteFileSuccessfully() {
        String imagePath = "path/to/image";

        doNothing().when(amazonS3).deleteObject(anyString(), anyString());

        s3FileStorageHandler.deleteFile(imagePath);

        verify(amazonS3, times(1)).deleteObject(anyString(), eq(imagePath));
    }

    @Test
    @DisplayName("파일 업로드 시 파일이 null인 경우 예외가 발생하는지 테스트")
    void uploadFileThrowsExceptionWhenFileIsNull() {
        String imagePath = "path/to/image";

        assertThrows(NullPointerException.class, () -> {
            s3FileStorageHandler.uploadFile(null, imagePath);
        });
    }

    @Test
    @DisplayName("파일 삭제 시 경로가 null인 경우 예외가 발생하지 않는지 테스트")
    void deleteFileDoesNotThrowExceptionWhenPathIsNull() {
        s3FileStorageHandler.deleteFile(null);
        verify(amazonS3, never()).deleteObject(anyString(), anyString());
    }

    @Test
    @DisplayName("파일 업로드 시 IOException이 발생하는 경우 CustomException이 발생하는지 테스트")
    void uploadFileThrowsCustomExceptionOnIOException() throws IOException {
        String imagePath = "path/to/image";
        String originalFileName = "path/to/image/1234567890abcdef.png";

        when(multipartFile.getOriginalFilename()).thenReturn("file.png");
        when(multipartFile.getSize()).thenReturn(100L);
        when(multipartFile.getContentType()).thenReturn("image/png");
        when(multipartFile.getInputStream()).thenThrow(new IOException());

        CustomException exception = assertThrows(CustomException.class, () -> {
            s3FileStorageHandler.uploadFile(multipartFile, imagePath);
        });

        assertEquals(IMAGE_UPLOAD_FAILED, exception.getErrorCode());
    }
}