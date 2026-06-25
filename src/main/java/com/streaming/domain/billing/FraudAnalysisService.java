package com.streaming.domain.billing;

import com.streaming.shared.exception.BusinessRuleViolationException;

import java.util.List;

public class FraudAnalysisService {

    private final List<FraudRule> rules;

    public FraudAnalysisService(List<FraudRule> rules) {
        if (rules == null || rules.isEmpty()) {
            throw new IllegalArgumentException("at least one fraud rule is required");
        }
        this.rules = List.copyOf(rules);
    }

    public void analyze(FraudAnalysisContext context) {
        for (FraudRule rule : rules) {
            if (!rule.validate(context)) {
                throw new BusinessRuleViolationException(rule.violationCode());
            }
        }
    }
}
