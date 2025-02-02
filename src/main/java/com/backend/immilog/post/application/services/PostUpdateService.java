package com.backend.immilog.post.application.services;

import com.backend.immilog.global.aop.lock.DistributedLock;
import com.backend.immilog.post.application.command.PostUpdateCommand;
import com.backend.immilog.post.application.services.command.BulkCommandService;
import com.backend.immilog.post.application.services.command.PostCommandService;
import com.backend.immilog.post.application.services.command.PostResourceCommandService;
import com.backend.immilog.post.application.services.query.PostQueryService;
import com.backend.immilog.post.domain.enums.PostType;
import com.backend.immilog.post.domain.enums.ResourceType;
import com.backend.immilog.post.domain.model.post.Post;
import com.backend.immilog.post.exception.PostErrorCode;
import com.backend.immilog.post.exception.PostException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class PostUpdateService {
    private final PostQueryService postQueryService;
    private final PostCommandService postCommandService;
    private final PostResourceCommandService postResourceCommandService;
    private final BulkCommandService bulkCommandService;

    final String VIEW_LOCK_KEY = "viewPost : ";

    public PostUpdateService(
            PostQueryService postQueryService,
            PostCommandService postCommandService,
            PostResourceCommandService postResourceCommandService,
            BulkCommandService bulkCommandService
    ) {
        this.postQueryService = postQueryService;
        this.postCommandService = postCommandService;
        this.postResourceCommandService = postResourceCommandService;
        this.bulkCommandService = bulkCommandService;
    }

    @Transactional
    public void updatePost(
            Long userId,
            Long postSeq,
            PostUpdateCommand command
    ) {
        Post post = postQueryService.getPostById(postSeq);
        if (!Objects.equals(post.userSeq(), userId)) {
            throw new PostException(PostErrorCode.NO_AUTHORITY);
        }
        Post updatedPost = post.updateTitle(command.title())
                .updateContent(command.content())
                .updateIsPublic(command.isPublic());
        updateResource(postSeq, command.deleteTags(), command.addTags(), ResourceType.TAG);
        updateResource(postSeq, command.deleteAttachments(), command.addAttachments(), ResourceType.ATTACHMENT);
        postCommandService.save(updatedPost);
    }

    @Async
    @DistributedLock(key = VIEW_LOCK_KEY, identifier = "#postSeq", expireTime = 5)
    public void increaseViewCount(Long postSeq) {
        Post post = postQueryService.getPostById(postSeq);
        Post updatedPost = post.increaseViewCount();
        postCommandService.save(updatedPost);
    }

    private void updateResource(
            Long postSeq,
            List<String> deleteResources,
            List<String> addResources,
            ResourceType resourceType
    ) {
        this.deleteResourceIfExists(postSeq, deleteResources, resourceType);
        this.addResourceIfExists(postSeq, addResources, resourceType);
    }

    private void deleteResourceIfExists(
            Long postSeq,
            List<String> deleteResources,
            ResourceType resourceType
    ) {
        if (deleteResources != null && !deleteResources.isEmpty()) {
            postResourceCommandService.deleteAllEntities(
                    postSeq,
                    PostType.POST,
                    resourceType,
                    deleteResources
            );
        }
    }

    private void addResourceIfExists(
            Long postSeq,
            List<String> addResources,
            ResourceType resourceType
    ) {
        if (addResources != null && !addResources.isEmpty()) {
            bulkCommandService.saveAll(
                    addResources,
                    """
                            INSERT INTO post_resource (
                                post_seq,
                                post_type,
                                resource_type,
                                content
                            ) VALUES (?, ?, ?, ?)
                            """,
                    (ps, resource) -> {
                        try {
                            ps.setLong(1, postSeq);
                            ps.setString(2, PostType.POST.name());
                            ps.setString(3, resourceType.name());
                            ps.setString(4, resource);
                        } catch (Exception e) {
                            log.error("Failed to save post resource", e);
                            throw new PostException(PostErrorCode.FAILED_TO_SAVE_POST);
                        }
                    }
            );
        }
    }
}
