package com.backend.immilog.post.infrastructure.repositories;

import com.backend.immilog.post.application.result.PostResult;
import com.backend.immilog.post.domain.enums.Categories;
import com.backend.immilog.post.domain.enums.Countries;
import com.backend.immilog.post.domain.enums.SortingMethods;
import com.backend.immilog.post.domain.model.interaction.InteractionUser;
import com.backend.immilog.post.domain.model.post.Post;
import com.backend.immilog.post.domain.model.resource.PostResource;
import com.backend.immilog.post.domain.repositories.InteractionUserRepository;
import com.backend.immilog.post.domain.repositories.PostRepository;
import com.backend.immilog.post.domain.repositories.PostResourceRepository;
import com.backend.immilog.post.infrastructure.jdbc.PostJdbcRepository;
import com.backend.immilog.post.infrastructure.jpa.entity.post.PostEntity;
import com.backend.immilog.post.infrastructure.jpa.repository.PostJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PostRepositoryImpl implements PostRepository {
    private final PostJdbcRepository postJdbcRepository;
    private final PostJpaRepository postJpaRepository;
    private final PostResourceRepository postResourceRepository;
    private final InteractionUserRepository interactionUserRepository;

    public PostRepositoryImpl(
            PostJdbcRepository postJdbcRepository,
            PostJpaRepository postJpaRepository,
            PostResourceRepository postResourceRepository,
            InteractionUserRepository interactionUserRepository
    ) {
        this.postJdbcRepository = postJdbcRepository;
        this.postJpaRepository = postJpaRepository;
        this.postResourceRepository = postResourceRepository;
        this.interactionUserRepository = interactionUserRepository;
    }

    @Override
    public Page<PostResult> getPosts(
            Countries country,
            SortingMethods sortingMethod,
            String isPublic,
            Categories category,
            Pageable pageable
    ) {
        Page<PostResult> postResults = postJdbcRepository.getPostResults(country, sortingMethod, isPublic, category, pageable);

        List<Long> postSeqList = postResults.stream()
                .map(PostResult::getSeq)
                .toList();

        List<PostResource> postResources = postResourceRepository.findAllByPostSeqList(postSeqList);
        List<InteractionUser> interactionUsers = interactionUserRepository.findAllByPostSeqList(postSeqList);

        return postResults.map(postResult -> {
            List<PostResource> resources = postResources.stream()
                    .filter(postResource -> postResource.getPostSeq().equals(postResult.getSeq()))
                    .toList();

            List<InteractionUser> interactionUserList = interactionUsers.stream()
                    .filter(interactionUser -> interactionUser.getPostSeq().equals(postResult.getSeq()))
                    .toList();

            postResult.addInteractionUsers(interactionUserList);
            postResult.addResources(resources);

            return postResult;
        });
    }

    @Override
    public Optional<PostResult> getPostDetail(Long postSeq) {
        Optional<PostResult> post = postJdbcRepository.getSinglePost(postSeq);
        List<Long> postSeqList = List.of(postSeq);

        List<PostResource> postResources = postResourceRepository.findAllByPostSeqList(postSeqList);
        List<InteractionUser> interactionUsers = interactionUserRepository.findAllByPostSeqList(postSeqList);

        return post.map(postResult -> {
            List<PostResource> resources = postResources.stream()
                    .filter(postResource -> postResource.getPostSeq().equals(postResult.getSeq()))
                    .toList();

            List<InteractionUser> interactionUserList = interactionUsers.stream()
                    .filter(interactionUser -> interactionUser.getPostSeq().equals(postResult.getSeq()))
                    .toList();

            postResult.addInteractionUsers(interactionUserList);
            postResult.addResources(resources);

            return postResult;
        });
    }

    @Override
    public Page<PostResult> getPostsByKeyword(
            String keyword,
            Pageable pageable
    ) {
        Page<PostResult> posts = postJdbcRepository.getPostsByKeyword(keyword, pageable);
        posts.getContent().forEach(post -> post.addKeywords(keyword));

        List<Long> postSeqList = posts.stream().map(PostResult::getSeq).toList();

        List<PostResource> postResources = postResourceRepository.findAllByPostSeqList(postSeqList);
        List<InteractionUser> interactionUsers = interactionUserRepository.findAllByPostSeqList(postSeqList);

        return posts.map(postResult -> {
            List<PostResource> resources = postResources.stream()
                    .filter(postResource -> postResource.getPostSeq().equals(postResult.getSeq()))
                    .toList();

            List<InteractionUser> interactionUserList = interactionUsers.stream()
                    .filter(interactionUser -> interactionUser.getPostSeq().equals(postResult.getSeq()))
                    .toList();

            postResult.addInteractionUsers(interactionUserList);
            postResult.addResources(resources);

            return postResult;
        });
    }

    @Override
    public Page<PostResult> getPostsByUserSeq(
            Long userSeq,
            Pageable pageable
    ) {
        Page<PostResult> posts = postJdbcRepository.getPostsByUserSeq(userSeq, pageable);
        List<Long> postSeqList = posts.stream()
                .map(PostResult::getSeq)
                .toList();

        List<PostResource> postResources = postResourceRepository.findAllByPostSeqList(postSeqList);
        List<InteractionUser> interactionUsers = interactionUserRepository.findAllByPostSeqList(postSeqList);

        return posts.map(postResult -> {
            List<PostResource> resources = postResources.stream()
                    .filter(postResource -> postResource.getPostSeq().equals(postResult.getSeq()))
                    .toList();

            List<InteractionUser> interactionUserList = interactionUsers.stream()
                    .filter(interactionUser -> interactionUser.getPostSeq().equals(postResult.getSeq()))
                    .toList();

            postResult.addInteractionUsers(interactionUserList);
            postResult.addResources(resources);

            return postResult;
        });
    }

    @Override
    public Optional<Post> getById(Long postSeq) {return postJpaRepository.findById(postSeq).map(PostEntity::toDomain);}

    @Override
    public Post save(Post post) {
        return postJpaRepository.save(PostEntity.from(post)).toDomain();
    }
}
