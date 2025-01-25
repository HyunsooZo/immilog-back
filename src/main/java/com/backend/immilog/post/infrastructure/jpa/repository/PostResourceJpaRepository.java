package com.backend.immilog.post.infrastructure.jpa.repository;

import com.backend.immilog.post.infrastructure.jpa.entity.resource.PostResourceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostResourceJpaRepository extends JpaRepository<PostResourceEntity, Long> {
    List<PostResourceEntity> findAllByPostSeq(Long seq);
}

