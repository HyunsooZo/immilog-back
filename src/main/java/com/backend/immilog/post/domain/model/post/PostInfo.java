package com.backend.immilog.post.domain.model.post;

import com.backend.immilog.post.domain.enums.Countries;
import com.backend.immilog.post.domain.enums.PostStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Builder
public record PostInfo(
        String title,
        String content,
        Long viewCount,
        Long likeCount,
        String region,
        PostStatus status,
        Countries country
){
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
