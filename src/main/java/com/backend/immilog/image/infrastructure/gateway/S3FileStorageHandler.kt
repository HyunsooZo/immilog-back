package com.backend.immilog.image.infrastructure.gateway

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.ObjectMetadata
import com.backend.immilog.global.exception.CommonErrorCode
import com.backend.immilog.global.exception.CustomException
import com.backend.immilog.notification.application.service.DiscordSendingService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.util.*

@Service
class S3FileStorageHandler(
    private val amazonS3: AmazonS3,
    private val discordSendingService: DiscordSendingService,
    @Value("\${cloud.aws.s3.bucket}") private val bucket: String = "default-bucket"
) : FileStorageHandler {

    override fun uploadFile(file: MultipartFile, imagePath: String): String {
        val originalFileName = this.generateFileName(file, imagePath)
        this.uploadFileToS3(file, originalFileName)
        return generateFileUrl(originalFileName)
    }

    override fun deleteFile(imagePath: String?) {
        if (imagePath.isNullOrBlank()) return
        amazonS3.deleteObject(bucket, imagePath)
    }

    private fun generateFileName(multipartFile: MultipartFile?, imagePath: String?): String {
        return "$imagePath/${randomUUIDString(16)}.${multipartFile?.extension}"
    }

    private fun uploadFileToS3(multipartFile: MultipartFile?, originalFileName: String) {
        val metadata = ObjectMetadata().apply {
            contentLength = multipartFile?.size ?: 0
            contentType = multipartFile?.contentType
        }

        try {
            amazonS3.putObject(bucket, originalFileName, multipartFile?.inputStream, metadata)
        } catch (e: IOException) {
            discordSendingService.send("S3 파일 업로드", e)
            throw CustomException(CommonErrorCode.IMAGE_UPLOAD_FAILED)
        }
    }

    private fun generateFileUrl(originalFileName: String): String =
        amazonS3.getUrl(bucket, originalFileName).toString()

    private fun randomUUIDString(length: Int): String =
        UUID.randomUUID().toString().replace("-", "").take(length)

    private val MultipartFile.extension: String
        get() = originalFilename?.substringAfterLast('.', "png") ?: "png"

}
