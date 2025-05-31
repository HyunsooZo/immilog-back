package com.backend.immilog.post.application.usecase;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.post.application.result.PostResult;
import com.backend.immilog.post.application.services.CommentQueryService;
import com.backend.immilog.post.application.services.InteractionUserQueryService;
import com.backend.immilog.post.application.services.PostQueryService;
import com.backend.immilog.post.domain.model.interaction.InteractionUser;
import com.backend.immilog.post.domain.model.post.Categories;
import com.backend.immilog.post.domain.model.post.PostType;
import com.backend.immilog.post.domain.model.post.SortingMethods;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

public interface PostFetchUseCase {
    Page<PostResult> getPosts(
            Country country,
            SortingMethods sortingMethod,
            String isPublic,
            Categories category,
            Integer page
    );

    PostResult getPostDetail(Long postSeq);

    List<PostResult> getBookmarkedPosts(
            Long userSeq,
            PostType postType
    );

    Page<PostResult> searchKeyword(
            String keyword,
            Integer page
    );

    Page<PostResult> getUserPosts(
            Long userSeq,
            Integer page
    );

    List<PostResult> getMostViewedPosts();

    List<PostResult> getHotPosts();

    @Slf4j
    @Service
    class PostFetcher implements PostFetchUseCase {
        private final PostQueryService postQueryService;
        private final CommentQueryService commentQueryService;
        private final InteractionUserQueryService interactionUserQueryService;

        public PostFetcher(
                PostQueryService postQueryService,
                CommentQueryService commentQueryService,
                InteractionUserQueryService interactionUserQueryService
        ) {
            this.postQueryService = postQueryService;
            this.commentQueryService = commentQueryService;
            this.interactionUserQueryService = interactionUserQueryService;
        }

        public Page<PostResult> getPosts(
                Country country,
                SortingMethods sortingMethod,
                String isPublic,
                Categories category,
                Integer page
        ) {
            final var pageable = PageRequest.of(Objects.requireNonNullElse(page, 0), 10);
            return postQueryService.getPosts(country, sortingMethod, isPublic, category, pageable);
        }

        @Transactional(readOnly = true)
        public PostResult getPostDetail(Long postSeq) {
            final var post = postQueryService.getPostDetail(postSeq);
            final var comments = commentQueryService.getComments(postSeq);
            post.addComments(comments);
            return post;
        }

        @Transactional(readOnly = true)
        public List<PostResult> getBookmarkedPosts(
                Long userSeq,
                PostType postType
        ) {
            final var bookmarks = interactionUserQueryService.getBookmarkInteractions(userSeq, postType);
            final var postSeqList = bookmarks.stream().map(InteractionUser::postSeq).toList();
            return postQueryService.getPostsByPostSeqList(postSeqList);
        }


        public Page<PostResult> searchKeyword(
                String keyword,
                Integer page
        ) {
            final var pageable = PageRequest.of(page, 10);
            final var posts = postQueryService.getPostsByKeyword(keyword, pageable);
            posts.getContent().forEach(post -> post.addKeywords(keyword));
            return posts;
        }

        public Page<PostResult> getUserPosts(
                Long userSeq,
                Integer page
        ) {
            final var pageable = PageRequest.of(Objects.requireNonNullElse(page, 0), 10);
            return postQueryService.getPostsByUserSeq(userSeq, pageable);
        }

        public List<PostResult> getMostViewedPosts() {
            return postQueryService.getPostsFromRedis("most_viewed_posts");
        }

        public List<PostResult> getHotPosts() {
            return postQueryService.getPostsFromRedis("hot_posts");
        }
    }
}
