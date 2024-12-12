package com.backend.immilog.post.infrastructure.jpa.entity;

import com.backend.immilog.post.domain.enums.InteractionType;
import com.backend.immilog.post.domain.enums.PostType;
import com.backend.immilog.post.domain.model.interaction.InteractionUser;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@DynamicUpdate
@Entity
@Table(name = "interaction_user")
public class InteractionUserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private Long postSeq;

    @Enumerated(EnumType.STRING)
    private PostType postType;

    @Enumerated(EnumType.STRING)
    private InteractionType interactionType;

    private Long userSeq;

    public static InteractionUserEntity from(
            InteractionUser interactionUser
    ) {
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
