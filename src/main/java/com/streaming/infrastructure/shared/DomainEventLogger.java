package com.streaming.infrastructure.shared;

import com.streaming.domain.billing.TransactionAuthorizedEvent;
import com.streaming.domain.subscription.SubscriptionActivatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class DomainEventLogger {

    private static final Logger log = LoggerFactory.getLogger(DomainEventLogger.class);

    @EventListener
    public void on(SubscriptionActivatedEvent event) {
        log.info("[DomainEvent] SubscriptionActivated user={} plan={} at={}",
                event.userId(), event.planId(), event.occurredOn());
    }

    @EventListener
    public void on(TransactionAuthorizedEvent event) {
        log.info("[DomainEvent] TransactionAuthorized card={} merchant={} at={}",
                event.cardId(), event.merchant(), event.occurredOn());
    }
}
