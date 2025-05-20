package com.backend.immilog.image.application

import com.backend.immilog.image.domain.Image
import com.backend.immilog.image.domain.ImageType
import com.backend.immilog.image.infrastructure.gateway.FileStorageHandler
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

interface ImageUploadUseCase{
    fun saveFiles(files: List<MultipartFile>, imagePath: String, imageType: ImageType): List<String>
    fun deleteFile(imagePath: String)
}

@Service
class ImageUploader(
    private val fileStorageHandler: FileStorageHandler,
    private val imageCommandService: ImageCommandService,
    private val imageQueryService: ImageQueryService
): ImageUploadUseCase {

    override fun saveFiles(
        files: List<MultipartFile>,
        imagePath: String,
        imageType: ImageType
    ): List<String> {
        return files.takeIf { it.isNotEmpty() }?.map { file ->
            val url = fileStorageHandler.uploadFile(file, imagePath)
            val fullPath = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(url)
                .build()
                .toUriString()
            imageCommandService.save(Image.of(fullPath, imageType))
            fullPath
        } ?: emptyList()
    }

    override fun deleteFile(imagePath: String) {
        imagePath.takeIf { it.isNotBlank() }?.let { path ->
            fileStorageHandler.deleteFile(path)
            val image = imageQueryService.getImageByPath(path)
            val deletedImage = image.delete()
            imageCommandService.save(deletedImage)
        }
    }
}
