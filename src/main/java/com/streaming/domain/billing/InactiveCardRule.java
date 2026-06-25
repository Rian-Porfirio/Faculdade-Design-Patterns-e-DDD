package com.streaming.domain.billing;

public class InactiveCardRule implements FraudRule {

    public static final String VIOLATION = "inactive-card";

    @Override
    public boolean validate(FraudAnalysisContext context) {
        return context.card().isActive();
    }

    @Override
    public String violationCode() {
        return VIOLATION;
    }
}
