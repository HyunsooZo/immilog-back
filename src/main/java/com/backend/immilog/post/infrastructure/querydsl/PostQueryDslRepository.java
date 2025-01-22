package com.backend.immilog.post.infrastructure.querydsl;

import com.backend.immilog.post.application.result.PostResult;
import com.backend.immilog.post.domain.enums.*;
import com.backend.immilog.post.infrastructure.jpa.entity.QInteractionUserEntity;
import com.backend.immilog.post.infrastructure.jpa.entity.QPostEntity;
import com.backend.immilog.post.infrastructure.jpa.entity.QPostResourceEntity;
import com.backend.immilog.post.infrastructure.result.PostEntityResult;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class PostQueryDslRepository {
    private final JPAQueryFactory queryFactory;

    public PostQueryDslRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public Page<PostResult> getPosts(
            Countries country,
            SortingMethods sortingMethod,
            String isPublic,
            Categories category,
            Pageable pageable
    ) {
        QPostEntity post = QPostEntity.postEntity;
        Predicate predicate = generateCriteria(country, isPublic, category, post);
        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(sortingMethod, post);

        List<PostEntityResult> postEntityResults = fetchPosts(
                post,
                predicate,
                orderSpecifier,
                pageable
        );

        long total = countPosts(post, predicate);

        return new PageImpl<>(
                postEntityResults.stream().map(PostEntityResult::toPostResult).toList(),
                pageable,
                total
        );
    }

    public Optional<PostResult> getPost(Long postSeq) {
        QPostEntity post = QPostEntity.postEntity;
        BooleanExpression predicate = post.seq.eq(postSeq);
        List<PostEntityResult> results = fetchPosts(post, predicate, null, null);
        return results.stream().map(PostEntityResult::toPostResult).findFirst();
    }

    public Page<PostResult> getPostsByKeyword(
            String keyword,
            Pageable pageable
    ) {
        QPostEntity post = QPostEntity.postEntity;
        BooleanExpression predicate = generateKeywordCriteria(keyword, post);
        List<PostEntityResult> postEntityResults = fetchPosts(post, predicate, null, pageable);
        long total = countPosts(post, predicate);
        return new PageImpl<>(
                postEntityResults.stream().map(PostEntityResult::toPostResult).toList(),
                pageable,
                total
        );
    }

    public Page<PostResult> getPostsByUserSeq(
            Long userSeq,
            Pageable pageable
    ) {
        QPostEntity post = QPostEntity.postEntity;

        BooleanExpression predicate = generateUserPostCriteria(userSeq, post);

        List<PostEntityResult> postEntityResults = fetchPosts(
                post,
                predicate,
                null,
                pageable
        );

        long total = countPosts(post, predicate);

        return new PageImpl<>(
                postEntityResults.stream().map(PostEntityResult::toPostResult).toList(),
                pageable,
                total
        );
    }

    private List<PostEntityResult> fetchPosts(
            QPostEntity post,
            Predicate predicate,
            OrderSpecifier<?> orderSpecifier,
            Pageable pageable
    ) {
        QPostResourceEntity resource = QPostResourceEntity.postResourceEntity;
        QInteractionUserEntity interUser = QInteractionUserEntity.interactionUserEntity;

        var query = queryFactory
                .select(post, Projections.list(interUser), Projections.list(resource))
                .from(post)
                .leftJoin(resource)
                .on(resource.postSeq.eq(post.seq).and(resource.postType.eq(PostType.POST)))
                .leftJoin(interUser)
                .on(interUser.postSeq.eq(post.seq).and(interUser.postType.eq(PostType.POST)))
                .where(predicate);

        if (orderSpecifier != null) {
            query.orderBy(orderSpecifier);
        }
        if (pageable != null) {
            query.offset(pageable.getOffset()).limit(pageable.getPageSize());
        }

        return query.transform(
                GroupBy.groupBy(post.seq).list(
                        Projections.constructor(
                                PostEntityResult.class,
                                post,
                                Projections.list(interUser),
                                Projections.list(resource)
                        )
                )
        );
    }

    private long countPosts(
            QPostEntity post,
            Predicate predicate
    ) {
        return Optional.ofNullable(
                queryFactory
                        .select(post.count())
                        .from(post)
                        .where(predicate)
                        .fetchOne()
        ).orElse(0L);
    }


    private BooleanExpression generateUserPostCriteria(
            Long userSeq,
            QPostEntity post
    ) {
        return post.postUserInfo.userSeq.eq(userSeq)
                .and(post.postInfo.status.ne(PostStatus.DELETED));
    }

    private BooleanExpression generateKeywordCriteria(
            String keyword,
            QPostEntity post
    ) {
        return post.postInfo.content.contains(keyword)
                .or(post.postInfo.title.contains(keyword));
    }

    private Predicate generateCriteria(
            Countries country,
            String isPublic,
            Categories category,
            QPostEntity post
    ) {
        BooleanBuilder predicateBuilder = new BooleanBuilder();

        if (!category.equals(Categories.ALL)) {
            predicateBuilder.and(post.category.eq(category));
        }
        if (!country.equals(Countries.ALL)) {
            predicateBuilder.and(post.postInfo.country.eq(country));
        }

        return predicateBuilder
                .and(post.isPublic.eq(isPublic))
                .and(post.postInfo.status.ne(PostStatus.DELETED))
                .getValue();
    }


    private OrderSpecifier<?> getOrderSpecifier(
            SortingMethods sortingMethod,
            QPostEntity post
    ) {
        return switch (sortingMethod) {
            case CREATED_DATE -> post.createdAt.desc();
            case VIEW_COUNT -> post.postInfo.viewCount.desc();
            case LIKE_COUNT -> post.postInfo.likeCount.desc();
            case COMMENT_COUNT -> post.commentCount.desc();
            default -> post.createdAt.desc();
        };
    }

    public List<PostResult> getPopularPosts(
            LocalDateTime from,
            LocalDateTime to,
            SortingMethods sortingMethod
    ) {
        QPostEntity post = QPostEntity.postEntity;

        BooleanBuilder predicateBuilder = new BooleanBuilder();
        Predicate predicate = predicateBuilder.and(post.createdAt.between(from, to)).getValue();

        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(sortingMethod, post);

        List<PostEntityResult> postEntityResults = fetchPosts(
                post,
                predicate,
                orderSpecifier,
                Pageable.ofSize(10)
        );

        return postEntityResults.stream()
                .map(PostEntityResult::toPostResult)
                .toList();
    }
}

