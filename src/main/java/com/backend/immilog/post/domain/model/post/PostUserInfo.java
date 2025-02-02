package com.backend.immilog.post.domain.model.post;

public record PostUserInfo(
        Long userSeq,
        String nickname,
        String profileImage
) {
}
