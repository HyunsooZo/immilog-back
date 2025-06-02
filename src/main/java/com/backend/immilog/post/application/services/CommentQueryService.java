package com.backend.immilog.post.application.services;

import com.backend.immilog.post.application.result.CommentResult;
import com.backend.immilog.post.domain.model.comment.Comment;
import com.backend.immilog.post.domain.model.interaction.InteractionUser;
import com.backend.immilog.post.domain.model.post.PostType;
import com.backend.immilog.post.domain.repositories.CommentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CommentQueryService {
    private final CommentRepository commentRepository;
    private final InteractionUserQueryService interactionUserQueryService;

    public CommentQueryService(
            CommentRepository commentRepository,
            InteractionUserQueryService interactionUserQueryService
    ) {
        this.commentRepository = commentRepository;
        this.interactionUserQueryService = interactionUserQueryService;
    }

    @Transactional(readOnly = true)
    public List<CommentResult> getComments(Long postSeq) {
        final List<CommentResult> comments = commentRepository.getComments(postSeq);
        final List<Long> list = comments.stream().map(CommentResult::seq).toList();
        final List<InteractionUser> interactionUsers = interactionUserQueryService.getInteractionUsersByPostSeqList(list, PostType.COMMENT);
        final Map<Long, Integer> likeCountMap = getLikeCountMap(interactionUsers);
        final Map<Long, List<Long>> likeUserMap = getLikeUsersMap(interactionUsers);
        return comments.stream().map(comment -> comment
                        .copyWithNewLikeCount(likeCountMap.getOrDefault(comment.seq(), 0))
                        .copyWithNewLikeUsers(likeUserMap.getOrDefault(comment.seq(), List.of())))
                .toList();
    }

    @Transactional(readOnly = true)
    public Comment getCommentById(Long commentSeq) {
        return commentRepository.findById(commentSeq);
    }

    private static Map<Long, List<Long>> getLikeUsersMap(List<InteractionUser> interactionUsers) {
        return interactionUsers.stream().collect(
                Collectors.groupingBy(
                        InteractionUser::postSeq,
                        Collectors.mapping(InteractionUser::userSeq, Collectors.toList())
                )
        );
    }

    private static Map<Long, Integer> getLikeCountMap(List<InteractionUser> interactionUsers) {
        return interactionUsers.stream().collect(
                Collectors.groupingBy(
                        InteractionUser::postSeq,
                        Collectors.collectingAndThen(Collectors.counting(), Long::intValue)
                )
        );
    }
}
