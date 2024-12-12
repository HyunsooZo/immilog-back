package com.backend.immilog.post.infrastructure.repositories;

import com.backend.immilog.post.application.result.JobBoardResult;
import com.backend.immilog.post.domain.enums.Countries;
import com.backend.immilog.post.domain.enums.Experience;
import com.backend.immilog.post.domain.enums.Industry;
import com.backend.immilog.post.domain.model.post.JobBoard;
import com.backend.immilog.post.domain.repositories.JobBoardRepository;
import com.backend.immilog.post.infrastructure.jpa.entity.JobBoardEntity;
import com.backend.immilog.post.infrastructure.jpa.repository.JobBoardJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JobBoardRepositoryImpl implements JobBoardRepository {
    private final JobBoardJpaRepository jobBoardJpaRepository;

    @Override
    public void save(
            JobBoard jobBoard
    ) {
        jobBoardJpaRepository.save(JobBoardEntity.from(jobBoard));
    }

    @Override
    public Page<JobBoardResult> getJobBoards(
            Countries country,
            String sortingMethod,
            Industry industry,
            Experience experience,
            Pageable pageable
    ) {
//        QJobBoardEntity jobBoard = QJobBoardEntity.jobBoardEntity;
//        QPostResourceEntity resource = QPostResourceEntity.postResourceEntity;
//        QInteractionUserEntity interUser = QInteractionUserEntity.interactionUserEntity;
//
//        BooleanBuilder predicateBuilder = new BooleanBuilder();
//        com.backend.immilog.user.domain.enums.Industry industryEnum = convertToUserIndustry(industry);
//
//        if (isNotNullAndSame(industry, Industry.ALL)) {
//            predicateBuilder.and(jobBoard.companyMetaData.industry.eq(industryEnum));
//        }
//        if (isNotNullAndSame(country, Countries.ALL)) {
//            predicateBuilder.and(jobBoard.postMetaData.country.eq(country));
//        }
//        if (isNotNullAndSame(experience, Experience.ALL)) {
//            predicateBuilder.and(jobBoard.companyMetaData.experience.eq(experience));
//        }
//
//        // 정렬 조건 설정
//        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(sortingMethod, jobBoard);
//
//        long total = getLength(predicateBuilder);
//        List<JobBoardResult> jobBoards = jpaQueryFactory
//                .select(jobBoard, list(interUser), list(resource))
//                .from(jobBoard)
//                .leftJoin(resource)
//                .on(resource.postSeq.eq(jobBoard.seq).and(resource.postType.eq(JOB_BOARD)))
//                .leftJoin(interUser)
//                .on(interUser.postSeq.eq(jobBoard.seq).and(interUser.postType.eq(JOB_BOARD)))
//                .where(predicateBuilder)
//                .orderBy(orderSpecifier)
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .transform(
//                        groupBy(jobBoard.seq).list(
//                                Projections.constructor(
//                                        JobBoardEntityResult.class,
//                                        jobBoard.seq,
//                                        jobBoard.postMetaData.title,
//                                        jobBoard.postMetaData.content,
//                                        jobBoard.postMetaData.viewCount,
//                                        jobBoard.postMetaData.likeCount,
//                                        list(resource),
//                                        list(interUser),
//                                        jobBoard.postMetaData.country,
//                                        jobBoard.postMetaData.region,
//                                        jobBoard.companyMetaData.companySeq,
//                                        jobBoard.companyMetaData.industry,
//                                        jobBoard.companyMetaData.deadline,
//                                        jobBoard.companyMetaData.experience,
//                                        jobBoard.companyMetaData.salary,
//                                        jobBoard.companyMetaData.company,
//                                        jobBoard.companyMetaData.companyEmail,
//                                        jobBoard.companyMetaData.companyPhone,
//                                        jobBoard.companyMetaData.companyAddress,
//                                        jobBoard.companyMetaData.companyHomepage,
//                                        jobBoard.companyMetaData.companyLogo,
//                                        jobBoard.postMetaData.status,
//                                        jobBoard.createdAt
//                                )
//                        )
//                )
//                .stream()
//                .map(JobBoardEntityResult::toResult)
//                .toList();
//
//        return new PageImpl<>(jobBoards, pageable, total);
        return null;
    }

    @Override
    public Optional<JobBoardResult> getJobBoardBySeq(
            Long jobBoardSeq
    ) {
        return null;
//        QJobBoardEntity jobBoard = QJobBoardEntity.jobBoardEntity;
//        QPostResourceEntity resource = QPostResourceEntity.postResourceEntity;
//        QInteractionUserEntity interUser = QInteractionUserEntity.interactionUserEntity;
//
//        return jpaQueryFactory
//                .select(jobBoard, list(interUser), list(resource))
//                .from(jobBoard)
//                .leftJoin(resource)
//                .on(resource.postSeq.eq(jobBoard.seq).and(resource.postType.eq(JOB_BOARD)))
//                .leftJoin(interUser)
//                .on(interUser.postSeq.eq(jobBoard.seq).and(interUser.postType.eq(JOB_BOARD)))
//                .where(jobBoard.seq.eq(jobBoardSeq))
//                .transform(
//                        groupBy(jobBoard.seq).list(
//                                Projections.constructor(
//                                        JobBoardEntityResult.class,
//                                        jobBoard.seq,
//                                        jobBoard.postMetaData.title,
//                                        jobBoard.postMetaData.content,
//                                        jobBoard.postMetaData.viewCount,
//                                        jobBoard.postMetaData.likeCount,
//                                        list(resource),
//                                        list(interUser),
//                                        jobBoard.postMetaData.country,
//                                        jobBoard.postMetaData.region,
//                                        jobBoard.companyMetaData.companySeq,
//                                        jobBoard.companyMetaData.industry,
//                                        jobBoard.companyMetaData.deadline,
//                                        jobBoard.companyMetaData.experience,
//                                        jobBoard.companyMetaData.salary,
//                                        jobBoard.companyMetaData.company,
//                                        jobBoard.companyMetaData.companyEmail,
//                                        jobBoard.companyMetaData.companyPhone,
//                                        jobBoard.companyMetaData.companyAddress,
//                                        jobBoard.companyMetaData.companyHomepage,
//                                        jobBoard.companyMetaData.companyLogo,
//                                        jobBoard.postMetaData.status,
//                                        jobBoard.createdAt
//                                )
//                        )
//                )
//                .stream()
//                .findFirst()
//                .map(JobBoardEntityResult::toResult);
    }
//
//    private Long getLength(
//            Predicate predicate
//    ) {
//        QJobBoardEntity jobBoard = QJobBoardEntity.jobBoardEntity;
//        return (long) jpaQueryFactory.selectFrom(jobBoard)
//                .where(predicate)
//                .fetch()
//                .size();
//    }
//
//    private OrderSpecifier<?> getOrderSpecifier(
//            String sortingMethod,
//            QJobBoardEntity jobBoard
//    ) {
//        return switch (SortingMethods.valueOf(sortingMethod)) {
//            case VIEW_COUNT -> jobBoard.postMetaData.viewCount.desc();
//            case LIKE_COUNT -> jobBoard.postMetaData.likeCount.desc();
//            default -> jobBoard.createdAt.desc();
//        };
//    }
//
//
//    private static com.backend.immilog.user.domain.enums.Industry convertToUserIndustry(Industry industry) {
//        return com.backend.immilog.user.domain.enums.Industry.valueOf(industry.name());
//    }
//
//    private <T, E extends Enum<E>> boolean isNotNullAndSame(
//            E value,
//            T compare
//    ) {
//        return Objects.equals(value, compare);
//    }

}