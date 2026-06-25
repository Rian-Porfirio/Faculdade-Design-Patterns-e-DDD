package com.streaming.domain.subscription;

import com.streaming.shared.exception.BusinessRuleViolationException;

import java.time.LocalDate;
import java.util.Optional;

public class SubscriptionActivationService {

    public static final String ACTIVE_SUBSCRIPTION_ALREADY_EXISTS = "active-subscription-already-exists";

    public Subscription activate(Long userId, Plan plan, Optional<Subscription> currentActive) {
        currentActive.ifPresent(active -> rejectIfSamePlan(active, plan));
        currentActive.ifPresent(Subscription::cancel);
        return Subscription.activate(userId, plan.id(), LocalDate.now(), plan.durationDays());
    }

    private void rejectIfSamePlan(Subscription active, Plan plan) {
        if (active.planId().equals(plan.id())) {
            throw new BusinessRuleViolationException(ACTIVE_SUBSCRIPTION_ALREADY_EXISTS);
        }
    }
}
