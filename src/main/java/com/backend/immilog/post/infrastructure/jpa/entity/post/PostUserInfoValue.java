package com.backend.immilog.post.infrastructure.jpa.entity.post;

import com.backend.immilog.post.domain.model.post.PostUserInfo;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class PostUserInfoValue {
    @Column(name = "user_seq")
    private Long userSeq;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "profile_image")
    private String profileImage;

    protected PostUserInfoValue() {}

    PostUserInfoValue(
            Long userSeq,
            String nickname,
            String profileImage
    ) {
        this.userSeq = userSeq;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }

    public static PostUserInfoValue of(
            Long userSeq,
            String nickname,
            String profileImage
    ) {
        return new PostUserInfoValue(
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
