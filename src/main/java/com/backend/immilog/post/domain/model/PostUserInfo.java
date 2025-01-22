package com.backend.immilog.post.domain.model;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter(AccessLevel.PROTECTED)
@Embeddable
public class PostUserInfo {
    private Long userSeq;
    private String nickname;
    private String profileImage;

    protected PostUserInfo() {}

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
