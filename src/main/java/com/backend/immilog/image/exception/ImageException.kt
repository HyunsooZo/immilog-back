package com.backend.immilog.image.exception

import com.backend.immilog.global.exception.CustomException
import com.backend.immilog.global.exception.ErrorCode

class ImageException(e: ErrorCode) : CustomException(e)
