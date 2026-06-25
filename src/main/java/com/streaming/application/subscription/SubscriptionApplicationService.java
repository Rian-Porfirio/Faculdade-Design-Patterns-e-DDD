package com.streaming.application.subscription;

import com.streaming.domain.identity.UserRepository;
import com.streaming.domain.subscription.Plan;
import com.streaming.domain.subscription.PlanRepository;
import com.streaming.domain.subscription.Subscription;
import com.streaming.domain.subscription.SubscriptionActivationService;
import com.streaming.domain.subscription.SubscriptionRepository;
import com.streaming.shared.application.DomainEventPublisher;
import com.streaming.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class SubscriptionApplicationService {

    private static final String SUBSCRIPTION_MERCHANT = "STREAMING-SUBSCRIPTION";

    private final SubscriptionRepository subscriptionRepository;
    private final PlanRepository planRepository;
    private final UserRepository userRepository;
    private final SubscriptionActivationService activationService;
    private final ChargePort chargePort;
    private final DomainEventPublisher eventPublisher;

    public SubscriptionApplicationService(SubscriptionRepository subscriptionRepository,
                                          PlanRepository planRepository,
                                          UserRepository userRepository,
                                          SubscriptionActivationService activationService,
                                          ChargePort chargePort,
                                          DomainEventPublisher eventPublisher) {
        this.subscriptionRepository = subscriptionRepository;
        this.planRepository = planRepository;
        this.userRepository = userRepository;
        this.activationService = activationService;
        this.chargePort = chargePort;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public Subscription subscribe(Long userId, Long planId, Long cardId) {
        ensureUserExists(userId);
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException("Plan", planId));

        Optional<Subscription> currentActive = subscriptionRepository.findActiveByUserId(userId);

        // Partnership: a subscription depends on a valid charge (antifraud runs here).
        chargePort.charge(cardId, plan.price(), SUBSCRIPTION_MERCHANT);

        Subscription activated = activationService.activate(userId, plan, currentActive);

        currentActive.ifPresent(subscriptionRepository::save);
        Subscription saved = subscriptionRepository.save(activated);
        eventPublisher.publishAll(activated.pullDomainEvents());
        return saved;
    }

    @Transactional(readOnly = true)
    public Subscription findById(Long id) {
        return subscriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription", id));
    }

    private void ensureUserExists(Long userId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new ResourceNotFoundException("User", userId);
        }
    }
}
