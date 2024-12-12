package com.backend.immilog.post.infrastructure.jpa.entity;

import com.backend.immilog.post.domain.enums.PostType;
import com.backend.immilog.post.domain.enums.ResourceType;
import com.backend.immilog.post.domain.model.resource.PostResource;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@DynamicUpdate
@Entity
@Table(name = "post_resource")
public class PostResourceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private Long postSeq;

    @Enumerated(EnumType.STRING)
    private PostType postType;

    @Enumerated(EnumType.STRING)
    private ResourceType resourceType;

    private String content;

    public static PostResourceEntity from(
            PostResource postResource
    ) {
        return PostResourceEntity.builder()
                .postSeq(postResource.getPostSeq())
                .postType(postResource.getPostType())
                .resourceType(postResource.getResourceType())
                .content(postResource.getContent())
                .build();
    }

    public PostResource toDomain() {
        return PostResource.builder()
                .postSeq(postSeq)
                .postType(postType)
                .resourceType(resourceType)
                .content(content)
                .build();
    }
}
