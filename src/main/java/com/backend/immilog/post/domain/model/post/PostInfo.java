package com.backend.immilog.post.domain.model.post;

import com.backend.immilog.post.domain.enums.Countries;
import com.backend.immilog.post.domain.enums.PostStatus;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Getter(AccessLevel.PROTECTED)
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class PostInfo {
    private String title;

    private String content;

    private Long viewCount;

    private Long likeCount;

    private String region;

    @Enumerated(EnumType.STRING)
    private PostStatus status;

    @Enumerated(EnumType.STRING)
    private Countries country;

    @Builder
    PostInfo(
            String title,
            String content,
            Long viewCount,
            Long likeCount,
            String region,
            PostStatus status,
            Countries country
    ) {
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.region = region;
        this.status = status;
        this.country = country;
    }

    public static <T extends Enum<T>> PostInfo of(
            String title,
            String content,
            T country,
            String region
    ) {
        return PostInfo.builder()
                .title(title)
                .content(content)
                .viewCount(0L)
                .likeCount(0L)
                .status(PostStatus.NORMAL)
                .country(Countries.valueOf(country.name()))
                .region(region)
                .build();
    }

}
