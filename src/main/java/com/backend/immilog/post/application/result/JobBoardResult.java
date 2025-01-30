package com.backend.immilog.post.application.result;

import com.backend.immilog.post.domain.enums.*;
import com.backend.immilog.post.domain.model.interaction.InteractionUser;
import com.backend.immilog.post.domain.model.resource.PostResource;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class JobBoardResult {
    private final Long seq;
    private final String title;
    private final String content;
    private final Long viewCount;
    private final Long likeCount;
    private final List<String> tags = new ArrayList<>();
    private final List<String> attachments = new ArrayList<>();
    private final List<Long> likeUsers = new ArrayList<>();
    private final List<Long> bookmarkUsers = new ArrayList<>();
    private final Countries country;
    private final String region;
    private final Industry industry;
    private final LocalDateTime deadline;
    private final Experience experience;
    private final String salary;
    private final Long companySeq;
    private final String companyName;
    private final String companyEmail;
    private final String companyPhone;
    private final String companyAddress;
    private final String companyHomepage;
    private final String companyLogo;
    private final Long companyManagerUserSeq;
    private final PostStatus status;
    private final LocalDateTime createdAt;

    @Builder
    public JobBoardResult(
            Long seq,
            String title,
            String content,
            Long viewCount,
            Long likeCount,
            List<String> tags,
            List<String> attachments,
            List<Long> likeUsers,
            List<Long> bookmarkUsers,
            Countries country,
            String region,
            Industry industry,
            LocalDateTime deadline,
            Experience experience,
            String salary,
            Long companySeq,
            String companyName,
            String companyEmail,
            String companyPhone,
            String companyAddress,
            String companyHomepage,
            String companyLogo,
            Long companyManagerUserSeq,
            PostStatus status,
            LocalDateTime createdAt
    ) {
        this.seq = seq;
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.tags.addAll(tags);
        this.attachments.addAll(attachments);
        this.likeUsers.addAll(likeUsers);
        this.bookmarkUsers.addAll(bookmarkUsers);
        this.country = country;
        this.region = region;
        this.industry = industry;
        this.deadline = deadline;
        this.experience = experience;
        this.salary = salary;
        this.companySeq = companySeq;
        this.companyName = companyName;
        this.companyEmail = companyEmail;
        this.companyPhone = companyPhone;
        this.companyAddress = companyAddress;
        this.companyHomepage = companyHomepage;
        this.companyLogo = companyLogo;
        this.companyManagerUserSeq = companyManagerUserSeq;
        this.status = status;
        this.createdAt = createdAt;
    }


    public void addInteractionUsers(List<InteractionUser> interactionUserList) {
        this.likeUsers.addAll(interactionUserList.stream()
                .filter(interactionUser -> interactionUser.getInteractionType().equals(InteractionType.LIKE))
                .map(InteractionUser::getUserSeq)
                .toList());
        this.bookmarkUsers.addAll(interactionUserList.stream()
                .filter(interactionUser -> interactionUser.getInteractionType().equals(InteractionType.BOOKMARK))
                .map(InteractionUser::getUserSeq)
                .toList()
        );
    }

    public void addResources(List<PostResource> resources) {
        this.tags.addAll(resources.stream()
                .filter(resource -> resource.getResourceType().equals(ResourceType.TAG))
                .map(PostResource::getContent)
                .toList()
        );
        this.attachments.addAll(resources.stream()
                .filter(resource -> resource.getResourceType().equals(ResourceType.ATTACHMENT))
                .map(PostResource::getContent)
                .toList()
        );
    }
}
