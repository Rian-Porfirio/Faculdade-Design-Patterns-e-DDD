package com.streaming.domain.subscription;

import com.streaming.shared.domain.DomainEvent;

import java.time.LocalDateTime;

public record SubscriptionActivatedEvent(
        Long userId,
        Long planId,
        LocalDateTime occurredOn
) implements DomainEvent {

    public SubscriptionActivatedEvent(Long userId, Long planId) {
        this(userId, planId, LocalDateTime.now());
    }
}
