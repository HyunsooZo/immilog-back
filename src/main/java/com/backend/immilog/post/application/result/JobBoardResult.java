package com.backend.immilog.post.application.result;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.post.domain.enums.*;
import com.backend.immilog.post.domain.model.interaction.InteractionUser;
import com.backend.immilog.post.domain.model.resource.PostResource;
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
    private final Country country;
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
            Country country,
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
        this.addStringList(tags, this.tags);
        this.addStringList(attachments, this.attachments);
        this.addLongList(likeUsers, this.likeUsers);
        this.addLongList(bookmarkUsers, this.bookmarkUsers);
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

    public JobBoardResult(
            Long seq,
            String title,
            String content,
            Long viewCount,
            Long likeCount,
            String region,
            PostStatus status,
            Country country,
            Industry industry,
            Experience experience,
            LocalDateTime deadline,
            String salary,
            Long companySeq,
            String companyName,
            String companyEmail,
            String companyPhone,
            String companyAddress,
            String companyHomepage,
            String companyLogo,
            Long companyManagerUserSeq,
            LocalDateTime createdAt
    ) {
        this.seq = seq;
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.region = region;
        this.status = status;
        this.country = country;
        this.industry = industry;
        this.experience = experience;
        this.deadline = deadline;
        this.salary = salary;
        this.companySeq = companySeq;
        this.companyName = companyName;
        this.companyEmail = companyEmail;
        this.companyPhone = companyPhone;
        this.companyAddress = companyAddress;
        this.companyHomepage = companyHomepage;
        this.companyLogo = companyLogo;
        this.companyManagerUserSeq = companyManagerUserSeq;
        this.createdAt = createdAt;
    }

    public void addInteractionUsers(List<InteractionUser> interactionUserList) {
        this.likeUsers.addAll(interactionUserList.stream()
                .filter(interactionUser -> interactionUser.interactionType().equals(InteractionType.LIKE))
                .map(InteractionUser::userSeq)
                .toList());
        this.bookmarkUsers.addAll(interactionUserList.stream()
                .filter(interactionUser -> interactionUser.interactionType().equals(InteractionType.BOOKMARK))
                .map(InteractionUser::userSeq)
                .toList()
        );
    }

    public void addResources(List<PostResource> resources) {
        this.tags.addAll(resources.stream()
                .filter(resource -> resource.resourceType().equals(ResourceType.TAG))
                .map(PostResource::content)
                .toList()
        );
        this.attachments.addAll(resources.stream()
                .filter(resource -> resource.resourceType().equals(ResourceType.ATTACHMENT))
                .map(PostResource::content)
                .toList()
        );
    }

    private void addLongList(
            List<Long> newLongList,
            List<Long> longFieldList
    ) {
        if (newLongList != null && newLongList.isEmpty()) {
            longFieldList.addAll(newLongList);
        }
    }

    private void addStringList(
            List<String> newList,
            List<String> stringFieldList
    ) {
        if (newList != null && newList.isEmpty()) {
            stringFieldList.addAll(newList);
        }
    }
}
