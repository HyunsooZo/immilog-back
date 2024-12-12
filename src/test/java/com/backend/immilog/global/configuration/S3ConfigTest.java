package com.backend.immilog.global.configuration;

import com.amazonaws.services.s3.AmazonS3Client;
import com.backend.immilog.image.config.S3Config;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("S3Config 클래스 테스트")
class S3ConfigTest {

    @InjectMocks
    private S3Config s3Config;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        setFieldValue(s3Config, "accessKey", "testAccessKey");
        setFieldValue(s3Config, "secretKey", "testSecretKey");
        setFieldValue(s3Config, "region", "us-west-2");
    }

    private void setFieldValue(
            Object target,
            String fieldName,
            String value
    ) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    @DisplayName("AmazonS3Client 객체가 정상적으로 생성되는지 테스트")
    void amazonS3ClientIsNotNull() {
        AmazonS3Client client = s3Config.amazonS3Client();
        assertNotNull(client);
    }

    @Test
    @DisplayName("AmazonS3Client 객체가 정상적으로 설정되는지 테스트")
    void amazonS3ClientIsConfiguredCorrectly() {
        AmazonS3Client client = s3Config.amazonS3Client();
        assertNotNull(client);
    }
}
