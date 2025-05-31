package com.backend.immilog.post.infrastructure.jpa.entity.resource;

import com.backend.immilog.post.domain.model.post.PostType;
import com.backend.immilog.post.domain.model.resource.PostResource;
import com.backend.immilog.post.domain.model.resource.ResourceType;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.DynamicUpdate;

@DynamicUpdate
@Entity
@Table(name = "post_resource")
public class PostResourceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long seq;

    @Column(name = "post_seq")
    private Long postSeq;

    @Column(name = "post_type")
    @Enumerated(EnumType.STRING)
    private PostType postType;

    @Getter
    @Column(name = "resource_type")
    @Enumerated(EnumType.STRING)
    private ResourceType resourceType;

    @Getter
    @Column(name = "content")
    private String content;

    protected PostResourceEntity() {}

    protected PostResourceEntity(
            Long seq,
            Long postSeq,
            PostType postType,
            ResourceType resourceType,
            String content
    ) {
        this.seq = seq;
        this.postSeq = postSeq;
        this.postType = postType;
        this.resourceType = resourceType;
        this.content = content;
    }

    public static PostResourceEntity from(PostResource postResource) {
        return new PostResourceEntity(
                postResource.seq(),
                postResource.postSeq(),
                postResource.postType(),
                postResource.resourceType(),
                postResource.content()
        );
    }

    public PostResource toDomain() {
        return new PostResource(
                this.seq,
                this.postSeq,
                this.postType,
                this.resourceType,
                this.content
        );
    }
}
