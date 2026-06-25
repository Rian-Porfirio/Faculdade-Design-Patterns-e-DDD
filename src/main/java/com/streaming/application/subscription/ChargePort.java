package com.streaming.application.subscription;

import com.streaming.shared.domain.Money;

public interface ChargePort {

    Long charge(Long cardId, Money amount, String merchant);
}
