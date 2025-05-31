package com.backend.immilog.post.infrastructure.jpa.entity.post;

import com.backend.immilog.post.domain.model.post.*;
import jakarta.persistence.*;
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
    private PostUserInfoValue postUserInfo;

    @Embedded
    private PostInfoValue postInfo;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private Categories category;

    @Column(name = "is_public")
    private String isPublic;

    @Column(name = "badge")
    private Badge badge;

    @Column(name = "comment_count")
    private Long commentCount;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    protected PostEntity() {}

    public PostEntity(
            Long seq,
            PostUserInfo postUserInfo,
            PostInfo postInfo,
            Categories category,
            String isPublic,
            Badge badge,
            Long commentCount,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        PostUserInfoValue postUserInfoValue = PostUserInfoValue.of(
                postUserInfo.userSeq(),
                postUserInfo.nickname(),
                postUserInfo.profileImage()
        );
        PostInfoValue postInfoValue = PostInfoValue.of(
                postInfo.title(),
                postInfo.content(),
                postInfo.viewCount(),
                postInfo.likeCount(),
                postInfo.country(),
                postInfo.region(),
                postInfo.status()
        );
        this.seq = seq;
        this.postUserInfo = postUserInfoValue;
        this.postInfo = postInfoValue;
        this.category = category;
        this.isPublic = isPublic;
        this.badge = badge;
        this.commentCount = commentCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static PostEntity from(Post post) {
        return new PostEntity(
                post.seq(),
                post.postUserInfo(),
                post.postInfo(),
                post.category(),
                post.isPublic(),
                post.badge(),
                post.commentCount(),
                post.createdAt(),
                post.seq() != null ? post.updatedAt() : null
        );
    }

    public Post toDomain() {
        return new Post(
                this.seq,
                this.postUserInfo == null ? null : this.postUserInfo.toDomain(),
                this.postInfo == null ? null :  this.postInfo.toDomain(),
                this.category,
                this.isPublic,
                this.badge,
                this.commentCount,
                this.createdAt,
                this.updatedAt
        );
    }
}
