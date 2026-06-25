package com.streaming.domain.subscription;

import com.streaming.shared.domain.AggregateRoot;

import java.time.LocalDate;

public class Subscription extends AggregateRoot {

    private Long id;
    private final Long userId;
    private final Long planId;
    private SubscriptionStatus status;
    private final LocalDate startDate;
    private LocalDate endDate;

    private Subscription(Long id, Long userId, Long planId, SubscriptionStatus status,
                         LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.userId = userId;
        this.planId = planId;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static Subscription activate(Long userId, Long planId, LocalDate startDate, int durationDays) {
        Subscription subscription = new Subscription(
                null, userId, planId, SubscriptionStatus.ACTIVE,
                startDate, startDate.plusDays(durationDays));
        subscription.registerEvent(new SubscriptionActivatedEvent(userId, planId));
        return subscription;
    }

    public static Subscription reconstitute(Long id, Long userId, Long planId, SubscriptionStatus status,
                                            LocalDate startDate, LocalDate endDate) {
        return new Subscription(id, userId, planId, status, startDate, endDate);
    }

    public void cancel() {
        this.status = SubscriptionStatus.CANCELLED;
        this.endDate = LocalDate.now();
    }

    public boolean isActive() {
        return status == SubscriptionStatus.ACTIVE;
    }

    public void assignId(Long id) {
        this.id = id;
    }

    public Long id() {
        return id;
    }

    public Long userId() {
        return userId;
    }

    public Long planId() {
        return planId;
    }

    public SubscriptionStatus status() {
        return status;
    }

    public LocalDate startDate() {
        return startDate;
    }

    public LocalDate endDate() {
        return endDate;
    }
}
