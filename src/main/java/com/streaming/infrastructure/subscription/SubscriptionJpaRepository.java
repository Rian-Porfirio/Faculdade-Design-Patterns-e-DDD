package com.streaming.infrastructure.subscription;

import com.streaming.domain.subscription.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubscriptionJpaRepository extends JpaRepository<SubscriptionJpaEntity, Long> {

    Optional<SubscriptionJpaEntity> findFirstByUserIdAndStatus(Long userId, SubscriptionStatus status);
}
