package com.backend.immilog.post.infrastructure.repositories;

import com.backend.immilog.post.domain.enums.InteractionType;
import com.backend.immilog.post.domain.enums.PostType;
import com.backend.immilog.post.domain.model.interaction.InteractionUser;
import com.backend.immilog.post.domain.repositories.InteractionUserRepository;
import com.backend.immilog.post.infrastructure.jpa.entity.InteractionUserEntity;
import com.backend.immilog.post.infrastructure.jpa.repository.InteractionUserJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class InteractionUserRepositoryImpl implements InteractionUserRepository {
    private final InteractionUserJpaRepository interactionUserJpaRepository;

    public InteractionUserRepositoryImpl(InteractionUserJpaRepository interactionUserJpaRepository) {
        this.interactionUserJpaRepository = interactionUserJpaRepository;
    }

    @Override
    public List<InteractionUser> getByPostSeq(Long postSeq) {
        return interactionUserJpaRepository.findByPostSeq(postSeq)
                .stream()
                .map(InteractionUserEntity::toDomain)
                .toList();
    }

    @Override
    public void delete(InteractionUser interactionUser) {
        interactionUserJpaRepository.delete(InteractionUserEntity.from(interactionUser));
    }

    @Override
    public void save(InteractionUser likeUser) {
        interactionUserJpaRepository.save(InteractionUserEntity.from(likeUser));
    }

    @Override
    public Optional<InteractionUser> getByPostSeqAndUserSeqAndPostTypeAndInteractionType(
            Long postSeq,
            Long userSeq,
            PostType postType,
            InteractionType interactionType
    ) {
        return interactionUserJpaRepository.findByPostSeqAndUserSeqAndPostTypeAndInteractionType(
                postSeq,
                userSeq,
                postType,
                interactionType
        ).map(InteractionUserEntity::toDomain);
    }
}
