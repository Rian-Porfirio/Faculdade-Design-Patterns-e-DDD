package com.streaming.infrastructure.subscription;

import com.streaming.domain.subscription.Subscription;
import com.streaming.domain.subscription.SubscriptionRepository;
import com.streaming.domain.subscription.SubscriptionStatus;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class SubscriptionRepositoryAdapter implements SubscriptionRepository {

    private final SubscriptionJpaRepository jpaRepository;

    public SubscriptionRepositoryAdapter(SubscriptionJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Subscription save(Subscription subscription) {
        return SubscriptionMapper.toDomain(jpaRepository.save(SubscriptionMapper.toJpa(subscription)));
    }

    @Override
    public Optional<Subscription> findById(Long id) {
        return jpaRepository.findById(id).map(SubscriptionMapper::toDomain);
    }

    @Override
    public Optional<Subscription> findActiveByUserId(Long userId) {
        return jpaRepository.findFirstByUserIdAndStatus(userId, SubscriptionStatus.ACTIVE)
                .map(SubscriptionMapper::toDomain);
    }
}
