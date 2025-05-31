package com.backend.immilog.post.infrastructure.jpa.entity.interaction;

import com.backend.immilog.post.domain.model.interaction.InteractionType;
import com.backend.immilog.post.domain.model.interaction.InteractionUser;
import com.backend.immilog.post.domain.model.post.PostType;
import jakarta.persistence.*;
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
        return new InteractionUserEntity(
                interactionUser.seq(),
                interactionUser.postSeq(),
                interactionUser.postType(),
                interactionUser.interactionType(),
                interactionUser.userSeq()
        );
    }

    public InteractionUser toDomain() {
        return new InteractionUser(
                this.seq,
                this.postSeq,
                this.postType,
                this.interactionType,
                this.userSeq
        );
    }
}
