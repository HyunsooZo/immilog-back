package com.backend.immilog.post.infrastructure.jpa.entity.interaction;

import com.backend.immilog.post.domain.enums.InteractionType;
import com.backend.immilog.post.domain.enums.PostType;
import com.backend.immilog.post.domain.model.interaction.InteractionUser;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.DynamicUpdate;

@DynamicUpdate
@Entity
@Table(name = "interaction_user")
public class InteractionUserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long seq;

    @Column(name = "post_seq")
    private Long postSeq;

    @Column(name="post_type")
    @Enumerated(EnumType.STRING)
    private PostType postType;

    @Getter
    @Column(name="interaction_type")
    @Enumerated(EnumType.STRING)
    private InteractionType interactionType;

    @Getter
    @Column(name="user_seq")
    private Long userSeq;

    protected InteractionUserEntity() {}

    @Builder
    protected InteractionUserEntity(
            Long seq,
            Long postSeq,
            PostType postType,
            InteractionType interactionType,
            Long userSeq
    ) {
        this.seq = seq;
        this.postSeq = postSeq;
        this.postType = postType;
        this.interactionType = interactionType;
        this.userSeq = userSeq;
    }

    public static InteractionUserEntity from(InteractionUser interactionUser) {
        return InteractionUserEntity.builder()
                .postSeq(interactionUser.getPostSeq())
                .postType(interactionUser.getPostType())
                .interactionType(interactionUser.getInteractionType())
                .userSeq(interactionUser.getUserSeq())
                .build();
    }

    public InteractionUser toDomain() {
        return InteractionUser.builder()
                .postSeq(postSeq)
                .postType(postType)
                .interactionType(interactionType)
                .userSeq(userSeq)
                .build();
    }
}
