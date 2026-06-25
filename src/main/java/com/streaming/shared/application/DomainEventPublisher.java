package com.streaming.shared.application;

import com.streaming.shared.domain.DomainEvent;

import java.util.List;

public interface DomainEventPublisher {

    void publish(DomainEvent event);

    default void publishAll(List<DomainEvent> events) {
        events.forEach(this::publish);
    }
}
