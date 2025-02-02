package com.backend.immilog.post.infrastructure.jpa.entity.post;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.post.domain.enums.PostStatus;
import com.backend.immilog.post.domain.model.post.PostInfo;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class PostInfoValue {
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
    private Country country;

    protected PostInfoValue() {}

    PostInfoValue(
            String title,
            String content,
            Long viewCount,
            Long likeCount,
            String region,
            PostStatus status,
            Country country
    ) {
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.region = region;
        this.status = status;
        this.country = country;
    }

    public static PostInfoValue of(
            String title,
            String content,
            Long viewCount,
            Long likeCount,
            Country country,
            String region,
            PostStatus status
    ) {
        return new PostInfoValue(
                title,
                content,
                viewCount,
                likeCount,
                region,
                status,
                Country.valueOf(country.name())
        );
    }

    public PostInfo toDomain() {
        return new PostInfo(
                title,
                content,
                viewCount,
                likeCount,
                region,
                status,
                country
        );
    }
}
