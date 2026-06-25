package com.streaming.application.billing;

import com.streaming.application.subscription.ChargePort;
import com.streaming.domain.billing.Transaction;
import com.streaming.shared.domain.Money;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionChargeAdapter implements ChargePort {

    private final TransactionApplicationService transactionApplicationService;

    public SubscriptionChargeAdapter(TransactionApplicationService transactionApplicationService) {
        this.transactionApplicationService = transactionApplicationService;
    }

    @Override
    public Long charge(Long cardId, Money amount, String merchant) {
        Transaction transaction = transactionApplicationService.authorize(cardId, amount, merchant);
        return transaction.id();
    }
}
