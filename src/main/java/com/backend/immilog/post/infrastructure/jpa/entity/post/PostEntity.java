package com.backend.immilog.post.infrastructure.jpa.entity.post;

import com.backend.immilog.post.domain.enums.Categories;
import com.backend.immilog.post.domain.enums.Countries;
import com.backend.immilog.post.domain.enums.PostStatus;
import com.backend.immilog.post.domain.model.post.Post;
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
    private PostUserInfoEntity postUserInfo;

    @Embedded
    private PostInfoEntity postInfo;

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
            Long userSeq,
            String nickname,
            String profileImage,
            String title,
            String content,
            Long viewCount,
            Long likeCount,
            String region,
            PostStatus status,
            Countries country,
            Categories category,
            String isPublic,
            Long commentCount,
            LocalDateTime updatedAt
    ) {
        this.seq = seq;
        this.postUserInfo = PostUserInfoEntity.of(userSeq, nickname, profileImage);
        this.postInfo = PostInfoEntity.of(title, content, country, region);
        this.category = category;
        this.isPublic = isPublic;
        this.commentCount = commentCount;
        this.updatedAt = updatedAt;
    }

    public static PostEntity from(Post post) {

        return PostEntity.builder()
                .seq(post.getSeq())
                .userSeq(post.getUserSeq())
                .nickname(post.getNickname())
                .profileImage(post.getProfileImage())
                .title(post.getTitle())
                .content(post.getContent())
                .country(Countries.valueOf(post.getCountryName()))
                .region(post.getRegion())
                .category(post.getCategory())
                .isPublic(post.getIsPublic())
                .commentCount(post.getCommentCount())
                .updatedAt(post.getSeq() != null ? LocalDateTime.now() : null)
                .build();
    }

    public Post toDomain() {
        return Post.builder()
                .seq(this.seq)
                .postUserInfo(this.postUserInfo.toDomain())
                .postData(this.postInfo.toDomain())
                .category(this.category)
                .isPublic(this.isPublic)
                .commentCount(this.commentCount)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
    }
}
