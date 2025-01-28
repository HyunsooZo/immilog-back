package com.backend.immilog.post.domain.model.post;

import lombok.Builder;

@Builder
public record PostUserInfo(
        Long userSeq,
        String nickname,
        String profileImage
) {
}
