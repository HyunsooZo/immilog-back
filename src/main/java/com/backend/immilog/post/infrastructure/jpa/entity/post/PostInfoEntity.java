package com.backend.immilog.post.infrastructure.jpa.entity.post;

import com.backend.immilog.post.domain.enums.Countries;
import com.backend.immilog.post.domain.enums.PostStatus;
import com.backend.immilog.post.domain.model.post.PostData;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class PostInfoEntity {
    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "view_count")
    private Long viewCount;

    @Column(name = "like_count")
    private Long likeCount;

    @Column(name = "region")
    private String region;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PostStatus status;

    @Column(name = "country")
    @Enumerated(EnumType.STRING)
    private Countries country;

    protected PostInfoEntity() {}

    PostInfoEntity(
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

    public static <T extends Enum<T>> PostInfoEntity of(
            String title,
            String content,
            T country,
            String region
    ) {
        return new PostInfoEntity(
                title,
                content,
                0L,
                0L,
                region,
                PostStatus.NORMAL,
                Countries.valueOf(country.name())
        );
    }

    public PostData toDomain() {
        return PostData.builder()
                .title(title)
                .content(content)
                .viewCount(viewCount)
                .likeCount(likeCount)
                .region(region)
                .status(status)
                .country(country)
                .build();
    }
}
