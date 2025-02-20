package com.backend.immilog.image.presentation.controller

import com.backend.immilog.image.application.service.ImageService
import com.backend.immilog.image.domain.enums.ImageType
import com.backend.immilog.image.presentation.payload.ImageRequest
import com.backend.immilog.image.presentation.payload.ImageResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@Tag(name = "Image API", description = "이미지 업로드 관련 API")
@RestController
@RequestMapping("/api/v1/images")
class ImageController(
    private val imageService: ImageService
) {

    @PostMapping
    @Operation(summary = "이미지 업로드", description = "이미지를 업로드합니다.")
    fun uploadImage(
        @Schema(description = "이미지 파일") @RequestParam("multipartFile") multipartFile: List<MultipartFile>,
        @Schema(description = "이미지 경로") @RequestParam("imagePath") imagePath: String,
        @Schema(description = "이미지 타입") @RequestParam("imageType") imageType: ImageType
    ): ResponseEntity<ImageResponse> {
        val data = imageService.saveFiles(multipartFile, imagePath, imageType)
        return ResponseEntity.ok(ImageResponse.of(data))
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteImage(@RequestBody imageRequest: ImageRequest): ResponseEntity<Unit> {
        imageService.deleteFile(imageRequest.imagePath)
        return ResponseEntity.noContent().build()
    }

}
