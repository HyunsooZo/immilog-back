package com.backend.immilog.post.domain.model.comment;

import com.backend.immilog.post.domain.enums.ReferenceType;

public record CommentRelation(
        Long postSeq,
        Long parentSeq,
        ReferenceType referenceType
) {
    public static CommentRelation of(
            Long postSeq,
            Long parentSeq,
            ReferenceType referenceType
    ) {
        return new CommentRelation(
                postSeq,
                parentSeq,
                referenceType
        );
    }
}
