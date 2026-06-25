package com.streaming.infrastructure.subscription;

import com.streaming.domain.subscription.Subscription;

final class SubscriptionMapper {

    private SubscriptionMapper() {
    }

    static SubscriptionJpaEntity toJpa(Subscription subscription) {
        return new SubscriptionJpaEntity(
                subscription.id(),
                subscription.userId(),
                subscription.planId(),
                subscription.status(),
                subscription.startDate(),
                subscription.endDate());
    }

    static Subscription toDomain(SubscriptionJpaEntity entity) {
        return Subscription.reconstitute(
                entity.getId(),
                entity.getUserId(),
                entity.getPlanId(),
                entity.getStatus(),
                entity.getStartDate(),
                entity.getEndDate());
    }
}
