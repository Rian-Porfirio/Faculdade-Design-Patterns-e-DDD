package com.streaming.domain.subscription;

import java.util.Optional;

public interface PlanRepository {

    Optional<Plan> findById(Long id);
}
