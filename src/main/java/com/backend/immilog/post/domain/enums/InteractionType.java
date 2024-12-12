package com.backend.immilog.post.domain.enums;

public enum InteractionType {
    LIKE,
    BOOKMARK;

    public static InteractionType convertToEnum(
            String interactionType
    ) {
        return switch (interactionType) {
            case "like" -> LIKE;
            case "bookmark" -> BOOKMARK;
            default -> throw new IllegalStateException("Unexpected value: " + interactionType);
        };
    }
}