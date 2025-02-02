package com.backend.immilog.post.domain.model.post;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.post.domain.enums.PostStatus;

public record PostInfo(
        String title,
        String content,
        Long viewCount,
        Long likeCount,
        String region,
        PostStatus status,
        Country country
){
    public static <T extends Enum<T>> PostInfo of(
            String title,
            String content,
            T country,
            String region
    ) {
        return new PostInfo(
                title,
                content,
                0L,
                0L,
                region,
                PostStatus.NORMAL,
                Country.valueOf(country.name())
        );
    }
}
