package com.backend.immilog.post.infrastructure.repositories;

import com.backend.immilog.post.domain.enums.InteractionType;
import com.backend.immilog.post.domain.enums.PostType;
import com.backend.immilog.post.domain.model.interaction.InteractionUser;
import com.backend.immilog.post.domain.repositories.InteractionUserRepository;
import com.backend.immilog.post.infrastructure.jdbc.InteractionUserJdbcRepository;
import com.backend.immilog.post.infrastructure.jpa.entity.interaction.InteractionUserEntity;
import com.backend.immilog.post.infrastructure.jpa.repository.InteractionUserJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class InteractionUserRepositoryImpl implements InteractionUserRepository {
    private final InteractionUserJpaRepository interactionUserJpaRepository;
    private final InteractionUserJdbcRepository interactionUserJdbcRepository;

    public InteractionUserRepositoryImpl(
            InteractionUserJpaRepository interactionUserJpaRepository,
            InteractionUserJdbcRepository interactionUserJdbcRepository
    ) {
        this.interactionUserJpaRepository = interactionUserJpaRepository;
        this.interactionUserJdbcRepository = interactionUserJdbcRepository;
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

    @Override
    public List<InteractionUser> getInteractionsByPostSeqList(
            List<Long> postSeqList,
            PostType postType
    ) {
        return interactionUserJdbcRepository.findAllByPostSeqList(postSeqList, postType);
    }

    @Override
    public List<InteractionUser> getInteractions(
            Long userSeq,
            PostType postType,
            InteractionType interactionType
    ) {
        return interactionUserJpaRepository.findByUserSeqAndPostTypeAndInteractionType(
                userSeq,
                postType,
                interactionType
        );
    }
}
