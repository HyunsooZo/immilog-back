package com.backend.immilog.image.presentation

import com.backend.immilog.image.application.ImageUploadUseCase
import com.backend.immilog.image.domain.ImageType
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@Tag(name = "Image API", description = "이미지 업로드 관련 API")
@RestController
@RequestMapping("/api/v1/images")
class ImageController(
    private val imageUploadUseCase: ImageUploadUseCase
) {
    @PostMapping(consumes = arrayOf(MediaType.MULTIPART_FORM_DATA_VALUE))
    @Operation(summary = "이미지 업로드", description = "이미지를 업로드합니다.")
    fun uploadImage(
        @Schema(description = "이미지 파일") @RequestParam("multipartFile") multipartFile: List<MultipartFile>,
        @Schema(description = "이미지 경로") @RequestParam("imagePath") imagePath: String,
        @Schema(description = "이미지 타입") @RequestParam("imageType") imageType: ImageType
    ): ResponseEntity<ImageResponse> {
        val data: List<String> = imageUploadUseCase.saveFiles(multipartFile, imagePath, imageType)
        return ResponseEntity.ok(ImageResponse.of(data))
    }

    @DeleteMapping(consumes = arrayOf(MediaType.MULTIPART_FORM_DATA_VALUE))
    @Operation(summary = "이미지 삭제", description = "이미지를 삭제합니다.")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteImage(@RequestBody imageRequest: ImageRequest): ResponseEntity<Unit> {
        imageUploadUseCase.deleteFile(imageRequest.imagePath, "")
        return ResponseEntity.noContent().build()
    }

}
