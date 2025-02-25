package com.backend.immilog.image.infrastructure.gateway

import org.springframework.web.multipart.MultipartFile

interface FileStorageHandler {
    fun uploadFile(file: MultipartFile, imagePath: String): String
    fun deleteFile(imagePath: String?)
}
