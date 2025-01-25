package com.backend.immilog.post.infrastructure.jpa.entity.comment;

import com.backend.immilog.post.domain.enums.ReferenceType;
import com.backend.immilog.post.domain.model.comment.CommentRelation;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class CommentRelationEntity {
    @Column(name = "post_seq")
    private Long postSeq;

    @Column(name = "parent_seq")
    private Long parentSeq;

    @Enumerated(EnumType.STRING)
    @Column(name = "reference_type")
    private ReferenceType referenceType;

    protected CommentRelationEntity() {}

    private CommentRelationEntity(
            Long postSeq,
            Long parentSeq,
            ReferenceType referenceType
    ) {
        this.postSeq = postSeq;
        this.parentSeq = parentSeq;
        this.referenceType = referenceType;
    }

    public static CommentRelationEntity of(
            Long postSeq,
            Long parentSeq,
            ReferenceType referenceType
    ) {
        return new CommentRelationEntity(postSeq, parentSeq, referenceType);
    }

    public CommentRelation toDomain() {
        return CommentRelation.of(postSeq, parentSeq, referenceType);
    }
}
