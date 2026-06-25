package com.streaming.domain.billing;

import java.time.Duration;

public class HighFrequencySmallIntervalRule implements FraudRule {

    public static final String VIOLATION = "high-frequency-small-interval";
    private static final int MAX_TRANSACTIONS = 3;
    private static final int WINDOW_MINUTES = 2;

    @Override
    public boolean validate(FraudAnalysisContext context) {
        Duration window = Duration.ofMinutes(WINDOW_MINUTES);
        long countWithinWindow = context.recentTransactions().stream()
                .filter(tx -> isWithinWindow(tx, context, window))
                .count();
        return countWithinWindow < MAX_TRANSACTIONS;
    }

    private boolean isWithinWindow(Transaction tx, FraudAnalysisContext context, Duration window) {
        Duration elapsed = Duration.between(tx.dateTime(), context.candidate().dateTime()).abs();
        return elapsed.compareTo(window) <= 0;
    }

    @Override
    public String violationCode() {
        return VIOLATION;
    }
}
