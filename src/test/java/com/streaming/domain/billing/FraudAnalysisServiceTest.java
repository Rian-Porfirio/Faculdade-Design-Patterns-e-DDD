package com.streaming.domain.billing;

import com.streaming.shared.domain.Money;
import com.streaming.shared.exception.BusinessRuleViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("FraudAnalysisService")
class FraudAnalysisServiceTest {

    private static final LocalDateTime NOW = LocalDateTime.of(2025, 1, 1, 12, 0);

    private Card activeCard() {
        return Card.reconstitute(1L, CardNumber.of("4111111111111111"), "Alice",
                "12/2030", "123", CardStatus.ACTIVE);
    }

    private Card inactiveCard() {
        return Card.reconstitute(1L, CardNumber.of("4111111111111111"), "Alice",
                "12/2030", "123", CardStatus.INACTIVE);
    }

    private FraudAnalysisService service() {
        return new FraudAnalysisService(List.of(
                new InactiveCardRule(),
                new HighFrequencySmallIntervalRule(),
                new DoubledTransactionRule()));
    }

    private Transaction candidate() {
        return Transaction.newCharge(1L, Money.of(new BigDecimal("10.00")), "STREAMING", NOW);
    }

    @Test
    @DisplayName("requires at least one rule")
    void requiresRules() {
        assertThatThrownBy(() -> new FraudAnalysisService(List.of()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("passes a clean transaction")
    void passesClean() {
        FraudAnalysisContext ctx = new FraudAnalysisContext(activeCard(), candidate(), List.of());
        assertThatCode(() -> service().analyze(ctx)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("stops at the first violated rule (inactive card)")
    void stopsAtFirstViolation() {
        FraudAnalysisContext ctx = new FraudAnalysisContext(inactiveCard(), candidate(), List.of());
        assertThatThrownBy(() -> service().analyze(ctx))
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessage("inactive-card");
    }
}
