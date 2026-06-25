package com.streaming.interfaces.subscription;

import com.streaming.domain.subscription.Subscription;

import java.time.LocalDate;

public record SubscriptionResponse(
        Long id,
        Long userId,
        Long planId,
        String status,
        LocalDate startDate,
        LocalDate endDate
) {
    public static SubscriptionResponse from(Subscription subscription) {
        return new SubscriptionResponse(
                subscription.id(),
                subscription.userId(),
                subscription.planId(),
                subscription.status().name(),
                subscription.startDate(),
                subscription.endDate()
        );
    }
}
