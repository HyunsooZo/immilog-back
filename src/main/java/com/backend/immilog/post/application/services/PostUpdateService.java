package com.backend.immilog.post.application.services;

import com.backend.immilog.global.aop.lock.DistributedLock;
import com.backend.immilog.post.application.command.PostUpdateCommand;
import com.backend.immilog.post.application.services.command.BulkCommandService;
import com.backend.immilog.post.application.services.command.PostCommandService;
import com.backend.immilog.post.application.services.command.PostResourceCommandService;
import com.backend.immilog.post.application.services.query.PostQueryService;
import com.backend.immilog.post.domain.enums.PostType;
import com.backend.immilog.post.domain.enums.ResourceType;
import com.backend.immilog.post.domain.model.Post;
import com.backend.immilog.post.exception.PostException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static com.backend.immilog.post.domain.enums.PostType.POST;
import static com.backend.immilog.post.domain.enums.ResourceType.ATTACHMENT;
import static com.backend.immilog.post.domain.enums.ResourceType.TAG;
import static com.backend.immilog.post.exception.PostErrorCode.*;

@Slf4j
@Service
public class PostUpdateService {
    final String VIEW_LOCK_KEY = "viewPost : ";
    private final PostQueryService postQueryService;
    private final PostCommandService postCommandService;
    private final PostResourceCommandService postResourceCommandService;
    private final BulkCommandService bulkCommandService;

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
            PostUpdateCommand postUpdateCommand
    ) {
        Post post = getPost(postSeq);
        validateAuthor(userId, post);
        updatePostInfo(post, postUpdateCommand);
        updateResources(postSeq, postUpdateCommand);
        postCommandService.save(post);
    }

    @Async
    @DistributedLock(key = VIEW_LOCK_KEY, identifier = "#postSeq", expireTime = 5)
    public void increaseViewCount(Long postSeq) {
        Post post = getPost(postSeq);
        post.increaseViewCount();
        postCommandService.save(post);
    }

    private void updateResources(
            Long postSeq,
            PostUpdateCommand request
    ) {
        updateResource(postSeq, request.deleteTags(), request.addTags(), TAG);
        updateResource(postSeq, request.deleteAttachments(), request.addAttachments(), ATTACHMENT);
    }

    private void updateResource(
            Long postSeq,
            List<String> deleteResources,
            List<String> addResources,
            ResourceType resourceType
    ) {
        deleteResourceIfExists(postSeq, POST, deleteResources, resourceType);
        addResourceIfExists(postSeq, POST, addResources, resourceType);
    }

    private void deleteResourceIfExists(
            Long postSeq,
            PostType postType,
            List<String> deleteResources,
            ResourceType resourceType
    ) {
        if (deleteResources != null && !deleteResources.isEmpty()) {
            postResourceCommandService.deleteAllEntities(
                    postSeq,
                    postType,
                    resourceType,
                    deleteResources
            );
        }
    }

    private void addResourceIfExists(
            Long postSeq,
            PostType postType,
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
                            ps.setString(2, postType.name());
                            ps.setString(3, resourceType.name());
                            ps.setString(4, resource);
                        } catch (Exception e) {
                            log.error("Failed to save post resource", e);
                            throw new PostException(FAILED_TO_SAVE_POST);
                        }
                    }
            );
        }
    }

    private void updatePostInfo(
            Post post,
            PostUpdateCommand request
    ) {
        if (request.title() != null) {
            post.updateTitle(request.title());
        }
        if (request.content() != null) {
            post.updateContent(request.content());
        }
        if (request.isPublic() != null) {
            post.updateIsPublic(request.isPublic() ? "Y" : "N");
        }
    }

    private void validateAuthor(
            Long userId,
            Post post
    ) {
        if (!Objects.equals(post.getUserSeq(), userId)) {
            throw new PostException(NO_AUTHORITY);
        }
    }

    private Post getPost(Long postSeq) {
        return postQueryService
                .getPostById(postSeq)
                .orElseThrow(() -> new PostException(POST_NOT_FOUND));
    }
}
