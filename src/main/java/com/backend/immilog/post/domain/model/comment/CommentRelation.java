package com.backend.immilog.post.domain.model.comment;

import com.backend.immilog.post.domain.enums.ReferenceType;
import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PROTECTED)
public class CommentRelation {
    private final Long postSeq;
    private final Long parentSeq;
    private final ReferenceType referenceType;

    private CommentRelation(
            Long postSeq,
            Long parentSeq,
            ReferenceType referenceType
    ) {
        this.postSeq = postSeq;
        this.parentSeq = parentSeq;
        this.referenceType = referenceType;
    }

    public static CommentRelation of(
            Long postSeq,
            Long parentSeq,
            ReferenceType referenceType
    ) {
        return new CommentRelation(postSeq, parentSeq, referenceType);
    }
}
