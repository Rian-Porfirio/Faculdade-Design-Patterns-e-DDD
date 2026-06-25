package com.streaming.domain.billing;

public interface FraudRule {

    boolean validate(FraudAnalysisContext context);

    String violationCode();
}
