package com.backend.immilog.post.domain.model.post;

import com.backend.immilog.post.domain.enums.Countries;
import com.backend.immilog.post.domain.enums.PostStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter(AccessLevel.PROTECTED)
public class PostData {
    private String title;
    private String content;
    private Long viewCount;
    private Long likeCount;
    private String region;
    private PostStatus status;
    private Countries country;

    @Builder
    PostData(
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

    public static <T extends Enum<T>> PostData of(
            String title,
            String content,
            T country,
            String region
    ) {
        return PostData.builder()
                .title(title)
                .content(content)
                .viewCount(0L)
                .likeCount(0L)
                .status(PostStatus.NORMAL)
                .country(Countries.valueOf(country.name()))
                .region(region)
                .build();
    }

    protected void increaseViewCount() {
        this.viewCount++;
    }

    protected void delete() {
        this.status = PostStatus.DELETED;
    }

    protected void updateTitle(String title) {
        this.title = title;
    }

    protected void updateContent(String content) {
        this.content = content;
    }
}
