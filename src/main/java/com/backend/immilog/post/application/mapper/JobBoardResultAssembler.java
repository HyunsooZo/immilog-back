package com.backend.immilog.post.application.mapper;

import com.backend.immilog.post.application.result.JobBoardResult;
import com.backend.immilog.post.domain.model.interaction.InteractionType;
import com.backend.immilog.post.domain.model.interaction.InteractionUser;
import com.backend.immilog.post.domain.model.resource.PostResource;
import com.backend.immilog.post.domain.model.resource.ResourceType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JobBoardResultAssembler {

    public void combineInteractionUsers(
            JobBoardResult jobBoardResult,
            List<InteractionUser> interactionUserList
    ) {
        jobBoardResult.likeUsers().addAll(interactionUserList.stream()
                .filter(interactionUser -> interactionUser.interactionType().equals(InteractionType.LIKE))
                .map(InteractionUser::userSeq)
                .toList());
        jobBoardResult.bookmarkUsers().addAll(interactionUserList.stream()
                .filter(interactionUser -> interactionUser.interactionType().equals(InteractionType.BOOKMARK))
                .map(InteractionUser::userSeq)
                .toList()
        );
    }

    public void combineResources(
            JobBoardResult jobBoardResult,
            List<PostResource> resources
    ) {
        jobBoardResult.tags().addAll(resources.stream()
                .filter(resource -> resource.resourceType().equals(ResourceType.TAG))
                .map(PostResource::content)
                .toList()
        );
        jobBoardResult.attachments().addAll(resources.stream()
                .filter(resource -> resource.resourceType().equals(ResourceType.ATTACHMENT))
                .map(PostResource::content)
                .toList()
        );
    }
}
