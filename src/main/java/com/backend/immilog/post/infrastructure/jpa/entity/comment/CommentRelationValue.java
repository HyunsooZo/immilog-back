package com.backend.immilog.post.infrastructure.jpa.entity.comment;

import com.backend.immilog.post.domain.enums.ReferenceType;
import com.backend.immilog.post.domain.model.comment.CommentRelation;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class CommentRelationValue {

    @Column(name = "post_seq")
    private Long postSeq;

    @Column(name = "parent_seq")
    private Long parentSeq;

    @Enumerated(EnumType.STRING)
    @Column(name = "reference_type")
    private ReferenceType referenceType;

    protected CommentRelationValue() {}

    private CommentRelationValue(
            Long postSeq,
            Long parentSeq,
            ReferenceType referenceType
    ) {
        this.postSeq = postSeq;
        this.parentSeq = parentSeq;
        this.referenceType = referenceType;
    }

    public static CommentRelationValue of(
            Long postSeq,
            Long parentSeq,
            ReferenceType referenceType
    ) {
        return new CommentRelationValue(postSeq, parentSeq, referenceType);
    }

    public CommentRelation toDomain() {
        return CommentRelation.of(postSeq, parentSeq, referenceType);
    }
}
