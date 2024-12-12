package com.backend.immilog.post.infrastructure.jpa.entity;

import com.backend.immilog.global.model.BaseDateEntity;
import com.backend.immilog.post.domain.enums.Categories;
import com.backend.immilog.post.domain.model.post.Post;
import com.backend.immilog.post.domain.model.post.PostInfo;
import com.backend.immilog.post.domain.model.post.PostUserInfo;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
@Entity
@Table(name = "post")
public class PostEntity extends BaseDateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Embedded
    private PostUserInfo postUserInfo;

    @Embedded
    private PostInfo postInfo;

    @Enumerated(EnumType.STRING)
    private Categories category;

    private String isPublic;

    private Long commentCount;

    @Builder
    PostEntity(
            Long seq,
            PostUserInfo postUserInfo,
            PostInfo postInfo,
            Categories category,
            String isPublic,
            Long commentCount
    ) {
        this.seq = seq;
        this.postUserInfo = postUserInfo;
        this.postInfo = postInfo;
        this.category = category;
        this.isPublic = isPublic;
        this.commentCount = commentCount;
    }

    public static PostEntity from(
            Post post
    ) {
        return PostEntity.builder()
                .seq(post.getSeq())
                .postUserInfo(post.getPostUserInfo())
                .postInfo(post.getPostInfo())
                .category(post.getCategory())
                .isPublic(post.getIsPublic())
                .commentCount(post.getCommentCount())
                .build();
    }

    public Post toDomain() {
        return Post.builder()
                .seq(this.seq)
                .postUserInfo(this.postUserInfo)
                .postInfo(this.postInfo)
                .category(this.category)
                .isPublic(this.isPublic)
                .commentCount(this.commentCount)
                .build();
    }
}
