package com.backend.immilog.image.exception

import com.backend.immilog.global.exception.ErrorCode
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.NOT_FOUND

enum class ImageErrorCode(
    private val status: HttpStatus,
    private val message: String
) : ErrorCode {
    IMAGE_NOT_FOUND(NOT_FOUND, "이미지를 찾을 수 없습니다."),
    INVALID_IMAGE_PATH(BAD_REQUEST, "이미지 경로가 올바르지 않습니다."),
    INVALID_IMAGE_TYPE(BAD_REQUEST, "이미지 타입이 올바르지 않습니다."),
    IMAGE_SAVE_FAILED(BAD_REQUEST, "이미지 저장에 실패했습니다.");

    override fun getStatus(): HttpStatus = status
    override fun getMessage(): String = message

}
