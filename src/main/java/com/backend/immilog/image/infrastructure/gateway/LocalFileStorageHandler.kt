package com.backend.immilog.image.infrastructure.gateway

import com.backend.immilog.global.exception.CommonErrorCode
import com.backend.immilog.global.exception.CustomException
import com.backend.immilog.notification.application.DiscordSendingService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.*

@Service
class LocalFileStorageHandler(
    private val discordSendingService: DiscordSendingService,
    @Value("\${local.file.storage.directory}")
    private val baseDirectory: String
) : FileStorageHandler {
    private val storageLocation: Path = Paths.get(baseDirectory).toAbsolutePath().normalize()

    init {
        try {
            Files.createDirectories(storageLocation)
        } catch (ex: IOException) {
            throw RuntimeException("업로드된 파일을 저장할 디렉토리를 생성하지 못했습니다.", ex)
        }
    }

    override fun uploadFile(file: MultipartFile, imagePath: String): String {
        val relativeFilePath = generateFileName(file, imagePath)
        try {
            val targetLocation = storageLocation.resolve(relativeFilePath)
            Files.createDirectories(targetLocation.parent)
            Files.copy(file.inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING)
        } catch (ex: IOException) {
            discordSendingService.send("Local 파일 업로드 실패", ex)
            throw CustomException(CommonErrorCode.IMAGE_UPLOAD_FAILED)
        }
        return generateFileUrl(relativeFilePath)
    }

    override fun deleteFile(imagePath: String?) {
        if (imagePath.isNullOrBlank()) return
        try {
            val targetLocation = storageLocation.resolve(imagePath)
            Files.deleteIfExists(targetLocation)
        } catch (ex: IOException) {
            discordSendingService.send("Local 파일 삭제 실패", ex)
            throw CustomException(CommonErrorCode.IMAGE_UPLOAD_FAILED)
        }
    }

    private fun generateFileName(file: MultipartFile, imagePath: String): String {
        return "$imagePath/${randomUUIDString(16)}.${file.extension}"
    }

    private fun generateFileUrl(relativeFilePath: String): String {
        return "/images/$relativeFilePath"
    }

    private fun randomUUIDString(length: Int): String =
        UUID.randomUUID().toString().replace("-", "").take(length)

    private val MultipartFile.extension: String
        get() = originalFilename?.substringAfterLast('.', "png") ?: "png"
}
