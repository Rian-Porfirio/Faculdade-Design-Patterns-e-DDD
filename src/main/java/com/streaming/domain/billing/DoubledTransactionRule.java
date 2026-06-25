package com.streaming.domain.billing;

import java.time.Duration;

public class DoubledTransactionRule implements FraudRule {

    public static final String VIOLATION = "doubled-transaction";
    private static final int MAX_SIMILAR = 2;
    private static final int WINDOW_MINUTES = 2;

    @Override
    public boolean validate(FraudAnalysisContext context) {
        Duration window = Duration.ofMinutes(WINDOW_MINUTES);
        Transaction candidate = context.candidate();
        long similarWithinWindow = context.recentTransactions().stream()
                .filter(tx -> isWithinWindow(tx, candidate, window))
                .filter(tx -> tx.hasSameValueAndMerchant(candidate.amount(), candidate.merchant()))
                .count();
        return similarWithinWindow < MAX_SIMILAR;
    }

    private boolean isWithinWindow(Transaction tx, Transaction candidate, Duration window) {
        Duration elapsed = Duration.between(tx.dateTime(), candidate.dateTime()).abs();
        return elapsed.compareTo(window) <= 0;
    }

    @Override
    public String violationCode() {
        return VIOLATION;
    }
}
