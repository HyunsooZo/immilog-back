package com.backend.immilog.image.exception;

import com.backend.immilog.global.exception.CustomException;
import com.backend.immilog.global.exception.ErrorCode;

public class ImageException extends CustomException {
    public ImageException(ErrorCode e) {
        super(e);
    }
}
