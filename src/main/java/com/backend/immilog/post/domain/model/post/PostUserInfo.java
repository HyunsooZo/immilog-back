package com.backend.immilog.post.domain.model.post;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter(AccessLevel.PROTECTED)
public class PostUserInfo {
    private Long userSeq;
    private String nickname;
    private String profileImage;

    @Builder
    PostUserInfo(
            Long userSeq,
            String nickname,
            String profileImage
    ) {
        this.userSeq = userSeq;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }
}
