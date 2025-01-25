package com.backend.immilog.post.application.services.command;

import com.backend.immilog.post.domain.model.interaction.InteractionUser;
import com.backend.immilog.post.domain.repositories.InteractionUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InteractionUserCommandService {
    private final InteractionUserRepository interactionUserRepository;

    public InteractionUserCommandService(InteractionUserRepository interactionUserRepository) {
        this.interactionUserRepository = interactionUserRepository;
    }

    @Transactional
    public void delete(InteractionUser interactionUser) {
        interactionUserRepository.delete(interactionUser);
    }

    @Transactional
    public void save(InteractionUser interactionUser) {
        interactionUserRepository.save(interactionUser);
    }
}
