package com.backend.immilog.post.domain.model.post;

import jakarta.persistence.Embeddable;
import lombok.*;

@Getter(AccessLevel.PROTECTED)
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
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
