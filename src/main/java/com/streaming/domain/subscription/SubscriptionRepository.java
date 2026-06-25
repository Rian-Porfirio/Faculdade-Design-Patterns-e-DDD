package com.streaming.domain.subscription;

import java.util.Optional;

public interface SubscriptionRepository {

    Subscription save(Subscription subscription);

    Optional<Subscription> findById(Long id);

    Optional<Subscription> findActiveByUserId(Long userId);
}
