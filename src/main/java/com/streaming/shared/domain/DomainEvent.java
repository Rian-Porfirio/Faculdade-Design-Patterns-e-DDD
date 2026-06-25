package com.streaming.shared.domain;

import java.time.LocalDateTime;

public interface DomainEvent {

    LocalDateTime occurredOn();
}
