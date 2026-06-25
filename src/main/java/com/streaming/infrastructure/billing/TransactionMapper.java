package com.streaming.infrastructure.billing;

import com.streaming.domain.billing.Transaction;
import com.streaming.shared.domain.Money;

final class TransactionMapper {

    private TransactionMapper() {
    }

    static TransactionJpaEntity toJpa(Transaction transaction) {
        return new TransactionJpaEntity(
                transaction.id(),
                transaction.cardId(),
                transaction.amount().amount(),
                transaction.amount().currency(),
                transaction.merchant(),
                transaction.dateTime(),
                transaction.status());
    }

    static Transaction toDomain(TransactionJpaEntity entity) {
        return Transaction.reconstitute(
                entity.getId(),
                entity.getCardId(),
                Money.of(entity.getAmountValue(), entity.getAmountCurrency()),
                entity.getMerchant(),
                entity.getDateTime(),
                entity.getStatus());
    }
}
