package com.streaming.domain.billing;

import com.streaming.shared.domain.AggregateRoot;
import com.streaming.shared.domain.Money;

import java.time.LocalDateTime;

public class Transaction extends AggregateRoot {

    private Long id;
    private final Long cardId;
    private final Money amount;
    private final String merchant;
    private final LocalDateTime dateTime;
    private TransactionStatus status;

    private Transaction(Long id, Long cardId, Money amount, String merchant,
                        LocalDateTime dateTime, TransactionStatus status) {
        if (cardId == null) {
            throw new IllegalArgumentException("cardId must not be null");
        }
        if (amount == null) {
            throw new IllegalArgumentException("amount must not be null");
        }
        if (merchant == null || merchant.isBlank()) {
            throw new IllegalArgumentException("merchant must not be blank");
        }
        this.id = id;
        this.cardId = cardId;
        this.amount = amount;
        this.merchant = merchant.trim();
        this.dateTime = dateTime;
        this.status = status;
    }

    public static Transaction newCharge(Long cardId, Money amount, String merchant, LocalDateTime dateTime) {
        return new Transaction(null, cardId, amount, merchant, dateTime, TransactionStatus.DECLINED);
    }

    public static Transaction reconstitute(Long id, Long cardId, Money amount, String merchant,
                                           LocalDateTime dateTime, TransactionStatus status) {
        return new Transaction(id, cardId, amount, merchant, dateTime, status);
    }

    public void authorize() {
        this.status = TransactionStatus.AUTHORIZED;
        registerEvent(new TransactionAuthorizedEvent(cardId, merchant, dateTime));
    }

    public boolean isAuthorized() {
        return status == TransactionStatus.AUTHORIZED;
    }

    public boolean hasSameValueAndMerchant(Money otherAmount, String otherMerchant) {
        return amount.hasSameValueAs(otherAmount) && merchant.equals(otherMerchant);
    }

    public void assignId(Long id) {
        this.id = id;
    }

    public Long id() {
        return id;
    }

    public Long cardId() {
        return cardId;
    }

    public Money amount() {
        return amount;
    }

    public String merchant() {
        return merchant;
    }

    public LocalDateTime dateTime() {
        return dateTime;
    }

    public TransactionStatus status() {
        return status;
    }
}
