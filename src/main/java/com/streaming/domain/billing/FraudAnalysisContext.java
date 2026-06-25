package com.streaming.domain.billing;

import java.util.Collections;
import java.util.List;

public final class FraudAnalysisContext {

    private final Card card;
    private final Transaction candidate;
    private final List<Transaction> recentTransactions;

    public FraudAnalysisContext(Card card, Transaction candidate, List<Transaction> recentTransactions) {
        this.card = card;
        this.candidate = candidate;
        this.recentTransactions = List.copyOf(recentTransactions);
    }

    public Card card() {
        return card;
    }

    public Transaction candidate() {
        return candidate;
    }

    public List<Transaction> recentTransactions() {
        return Collections.unmodifiableList(recentTransactions);
    }
}
