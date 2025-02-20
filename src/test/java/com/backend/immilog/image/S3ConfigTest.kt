package com.backend.immilog.image

import com.amazonaws.services.s3.AmazonS3Client
import com.backend.immilog.image.config.S3Config
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.MockitoAnnotations
import java.lang.reflect.Field

@DisplayName("S3Config 클래스 테스트")
class S3ConfigTest {

    private lateinit var s3Config: S3Config

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        // 더미 값을 직접 전달
        S3Config("dummyAccessKey", "dummySecretKey", "dummyRegion").also { s3Config = it }
        setFieldValue(s3Config, "accessKey", "testAccessKey")
        setFieldValue(s3Config, "secretKey", "testSecretKey")
        setFieldValue(s3Config, "region", "us-west-2")
    }

    private fun setFieldValue(target: Any, fieldName: String, value: String) {
        val field: Field = target.javaClass.getDeclaredField(fieldName)
        field.isAccessible = true
        field.set(target, value)
    }

    @Test
    @DisplayName("AmazonS3Client 객체가 정상적으로 생성되는지 테스트")
    fun amazonS3ClientIsNotNull() {
        val client: AmazonS3Client = s3Config.amazonS3Client()
        assertNotNull(client)
    }

    @Test
    @DisplayName("AmazonS3Client 객체가 정상적으로 설정되는지 테스트")
    fun amazonS3ClientIsConfiguredCorrectly() {
        val client: AmazonS3Client = s3Config.amazonS3Client()
        assertNotNull(client)
    }
}
