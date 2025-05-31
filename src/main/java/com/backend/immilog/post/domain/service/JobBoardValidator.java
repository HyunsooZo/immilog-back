package com.backend.immilog.post.domain.service;

import com.backend.immilog.post.exception.PostException;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.backend.immilog.post.exception.PostErrorCode.NO_AUTHORITY;

@Service
public class JobBoardValidator {

    public void validateOwner(
            Long userSeq,
            Long companyManagerUserSeq
    ) {
        if (!Objects.equals(companyManagerUserSeq, userSeq)) {
            throw new PostException(NO_AUTHORITY);
        }
    }
}
