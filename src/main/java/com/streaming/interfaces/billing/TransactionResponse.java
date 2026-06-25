package com.streaming.interfaces.billing;

import com.streaming.domain.billing.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponse(
        Long id,
        Long cardId,
        BigDecimal amount,
        String currency,
        String merchant,
        LocalDateTime dateTime,
        String status
) {
    public static TransactionResponse from(Transaction transaction) {
        return new TransactionResponse(
                transaction.id(),
                transaction.cardId(),
                transaction.amount().amount(),
                transaction.amount().currency(),
                transaction.merchant(),
                transaction.dateTime(),
                transaction.status().name()
        );
    }
}
