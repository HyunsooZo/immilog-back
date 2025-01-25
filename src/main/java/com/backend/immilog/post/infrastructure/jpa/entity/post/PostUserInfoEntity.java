package com.backend.immilog.post.infrastructure.jpa.entity.post;

import com.backend.immilog.post.domain.model.post.PostUserInfo;
import jakarta.persistence.Embeddable;

@Embeddable
public class PostUserInfoEntity {
    private Long userSeq;
    private String nickname;
    private String profileImage;

    protected PostUserInfoEntity() {}

    PostUserInfoEntity(
            Long userSeq,
            String nickname,
            String profileImage
    ) {
        this.userSeq = userSeq;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }

    public static PostUserInfoEntity of(
            Long userSeq,
            String nickname,
            String profileImage
    ) {
        return new PostUserInfoEntity(
                userSeq,
                nickname,
                profileImage
        );
    }

    public PostUserInfo toDomain() {
        return PostUserInfo.builder()
                .userSeq(userSeq)
                .nickname(nickname)
                .profileImage(profileImage)
                .build();
    }
}
