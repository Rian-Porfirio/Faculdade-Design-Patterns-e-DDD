package com.streaming.shared.exception;

public class BusinessRuleViolationException extends RuntimeException {

    public BusinessRuleViolationException(String violationCode) {
        super(violationCode);
    }
}
