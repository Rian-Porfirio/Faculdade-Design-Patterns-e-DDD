package com.streaming.domain.billing;

import com.streaming.shared.domain.DomainEvent;

import java.time.LocalDateTime;

public record TransactionAuthorizedEvent(
        Long cardId,
        String merchant,
        LocalDateTime occurredOn
) implements DomainEvent {
}
