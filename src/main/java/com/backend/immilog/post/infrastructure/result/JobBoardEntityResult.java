package com.backend.immilog.post.infrastructure.result;

import com.backend.immilog.post.application.result.JobBoardResult;
import com.backend.immilog.post.domain.enums.*;
import com.backend.immilog.post.infrastructure.jpa.entity.InteractionUserEntity;
import com.backend.immilog.post.infrastructure.jpa.entity.PostResourceEntity;
import com.backend.immilog.user.domain.enums.Industry;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

import static com.backend.immilog.post.domain.enums.InteractionType.BOOKMARK;
import static com.backend.immilog.post.domain.enums.InteractionType.LIKE;
import static com.backend.immilog.post.domain.enums.ResourceType.ATTACHMENT;
import static com.backend.immilog.post.domain.enums.ResourceType.TAG;

@Getter
@Setter
public class JobBoardEntityResult {
    private Long seq;
    private String title;
    private String content;
    private Long viewCount;
    private Long likeCount;
    private List<String> tags;
    private List<String> attachments;
    private List<Long> likeUsers;
    private List<Long> bookmarkUsers;
    private Countries country;
    private String region;
    private Long companySeq;
    private Industry industry;
    private LocalDateTime deadline;
    private Experience experience;
    private String salary;
    private String companyName;
    private String companyEmail;
    private String companyPhone;
    private String companyAddress;
    private String companyHomepage;
    private String companyLogo;
    private PostStatus status;
    private LocalDateTime createdAt;

    public JobBoardEntityResult(
            Long seq,
            String title,
            String content,
            Long viewCount,
            Long likeCount,
            List<PostResourceEntity> resources,
            List<InteractionUserEntity> interUsers,
            Countries country,
            String region,
            Long companySeq,
            Industry industry,
            LocalDateTime deadline,
            Experience experience,
            String salary,
            String companyName,
            String companyEmail,
            String companyPhone,
            String companyAddress,
            String companyHomepage,
            String companyLogo,
            PostStatus status,
            LocalDateTime createdAt
    ) {
        List<String> attachments = getStrings(resources, ATTACHMENT);
        List<String> tags = getStrings(resources, TAG);
        List<Long> likeUsers = getLongs(interUsers, LIKE);
        List<Long> bookmarkUsers = getLongs(interUsers, BOOKMARK);

        this.seq = seq;
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.tags = tags;
        this.attachments = attachments;
        this.likeUsers = likeUsers;
        this.bookmarkUsers = bookmarkUsers;
        this.country = country;
        this.region = region;
        this.companySeq = companySeq;
        this.industry = industry;
        this.deadline = deadline;
        this.experience = experience;
        this.salary = salary;
        this.companyName = companyName;
        this.companyEmail = companyEmail;
        this.companyPhone = companyPhone;
        this.companyAddress = companyAddress;
        this.companyHomepage = companyHomepage;
        this.companyLogo = companyLogo;
        this.status = status;
        this.createdAt = createdAt;
    }

    private static List<Long> getLongs(
            List<InteractionUserEntity> interUsers,
            InteractionType like
    ) {
        return interUsers.stream()
                .filter(interUser -> interUser.getPostType().equals(PostType.JOB_BOARD))
                .filter(interUser -> interUser.getInteractionType().equals(like))
                .map(InteractionUserEntity::getUserSeq)
                .toList();
    }

    private static List<String> getStrings(
            List<PostResourceEntity> resources,
            ResourceType attachment
    ) {
        return resources.stream()
                .filter(resource -> resource.getPostType().equals(PostType.JOB_BOARD))
                .filter(resource -> resource.getResourceType().equals(attachment))
                .map(PostResourceEntity::getContent)
                .toList();
    }

    public JobBoardResult toResult(
    ) {
        return JobBoardResult.builder()
                .seq(seq)
                .title(title)
                .content(content)
                .viewCount(viewCount)
                .likeCount(likeCount)
                .tags(tags)
                .attachments(attachments)
                .likeUsers(likeUsers)
                .bookmarkUsers(bookmarkUsers)
                .country(country)
                .region(region)
                .industry(industry)
                .deadline(deadline)
                .experience(experience)
                .salary(salary)
                .companySeq(companySeq)
                .companyName(companyName)
                .companyEmail(companyEmail)
                .companyPhone(companyPhone)
                .companyAddress(companyAddress)
                .companyHomepage(companyHomepage)
                .companyLogo(companyLogo)
                .status(status)
                .createdAt(createdAt)
                .build();

    }
}
