package com.streaming.interfaces.subscription;

import jakarta.validation.constraints.NotNull;

public record CreateSubscriptionRequest(
        @NotNull(message = "userId must not be null")
        Long userId,

        @NotNull(message = "planId must not be null")
        Long planId,

        @NotNull(message = "cardId must not be null")
        Long cardId
) {
}
