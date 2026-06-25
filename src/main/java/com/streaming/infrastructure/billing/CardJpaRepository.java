package com.streaming.infrastructure.billing;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CardJpaRepository extends JpaRepository<CardJpaEntity, Long> {
}
