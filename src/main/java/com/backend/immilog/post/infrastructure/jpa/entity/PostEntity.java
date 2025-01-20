package com.backend.immilog.post.infrastructure.jpa.entity;

import com.backend.immilog.post.domain.enums.Categories;
import com.backend.immilog.post.domain.model.post.Post;
import com.backend.immilog.post.domain.model.post.PostInfo;
import com.backend.immilog.post.domain.model.post.PostUserInfo;
import jakarta.persistence.*;
import lombok.Builder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@DynamicUpdate
@DynamicInsert
@Entity
@Table(name = "post")
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long seq;

    @Embedded
    private PostUserInfo postUserInfo;

    @Embedded
    private PostInfo postInfo;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private Categories category;

    @Column(name = "is_public")
    private String isPublic;

    @Column(name = "comment_count")
    private Long commentCount;

    @Column(name = "created_at")
    private final LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    protected PostEntity() {}

    @Builder
    protected PostEntity(
            Long seq,
            PostUserInfo postUserInfo,
            PostInfo postInfo,
            Categories category,
            String isPublic,
            Long commentCount,
            LocalDateTime updatedAt
    ) {
        this.seq = seq;
        this.postUserInfo = postUserInfo;
        this.postInfo = postInfo;
        this.category = category;
        this.isPublic = isPublic;
        this.commentCount = commentCount;
        this.updatedAt = updatedAt;
    }

    public static PostEntity from(Post post) {
        return PostEntity.builder()
                .seq(post.getSeq())
                .postUserInfo(post.getPostUserInfo())
                .postInfo(post.getPostInfo())
                .category(post.getCategory())
                .isPublic(post.getIsPublic())
                .commentCount(post.getCommentCount())
                .updatedAt(post.getSeq() != null ? LocalDateTime.now() : null)
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
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
    }
}
