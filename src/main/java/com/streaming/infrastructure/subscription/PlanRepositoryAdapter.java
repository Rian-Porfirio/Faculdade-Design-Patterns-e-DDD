package com.streaming.infrastructure.subscription;

import com.streaming.domain.subscription.Plan;
import com.streaming.domain.subscription.PlanRepository;
import com.streaming.shared.domain.Money;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PlanRepositoryAdapter implements PlanRepository {

    private final PlanJpaRepository jpaRepository;

    public PlanRepositoryAdapter(PlanJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Plan> findById(Long id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    private Plan toDomain(PlanJpaEntity entity) {
        return Plan.reconstitute(
                entity.getId(),
                entity.getName(),
                Money.of(entity.getPriceAmount(), entity.getPriceCurrency()),
                entity.getDurationDays());
    }
}
