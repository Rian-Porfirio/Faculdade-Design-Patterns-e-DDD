package com.streaming.infrastructure.subscription;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanJpaRepository extends JpaRepository<PlanJpaEntity, Long> {
}
