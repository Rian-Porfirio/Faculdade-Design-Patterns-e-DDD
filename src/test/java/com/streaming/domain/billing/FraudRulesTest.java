package com.streaming.domain.billing;

import com.streaming.shared.domain.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Antifraud Rules")
class FraudRulesTest {

    private static final LocalDateTime NOW = LocalDateTime.of(2025, 1, 1, 12, 0);
    private static final Money TEN = Money.of(new BigDecimal("10.00"));
    private static final String MERCHANT = "STREAMING";

    private Card activeCard() {
        return Card.reconstitute(1L, CardNumber.of("4111111111111111"), "Alice",
                "12/2030", "123", CardStatus.ACTIVE);
    }

    private Card inactiveCard() {
        return Card.reconstitute(1L, CardNumber.of("4111111111111111"), "Alice",
                "12/2030", "123", CardStatus.INACTIVE);
    }

    private Transaction candidate() {
        return Transaction.newCharge(1L, TEN, MERCHANT, NOW);
    }

    private Transaction past(Money amount, String merchant, int minutesAgo) {
        return Transaction.reconstitute(null, 1L, amount, merchant,
                NOW.minusMinutes(minutesAgo), TransactionStatus.AUTHORIZED);
    }

    @Nested
    @DisplayName("InactiveCardRule")
    class InactiveCard {

        private final InactiveCardRule rule = new InactiveCardRule();

        @Test
        @DisplayName("passes for an active card")
        void passesActive() {
            FraudAnalysisContext ctx = new FraudAnalysisContext(activeCard(), candidate(), List.of());
            assertThat(rule.validate(ctx)).isTrue();
        }

        @Test
        @DisplayName("fails for an inactive card")
        void failsInactive() {
            FraudAnalysisContext ctx = new FraudAnalysisContext(inactiveCard(), candidate(), List.of());
            assertThat(rule.validate(ctx)).isFalse();
            assertThat(rule.violationCode()).isEqualTo("inactive-card");
        }
    }

    @Nested
    @DisplayName("HighFrequencySmallIntervalRule")
    class HighFrequency {

        private final HighFrequencySmallIntervalRule rule = new HighFrequencySmallIntervalRule();

        @Test
        @DisplayName("passes with up to three transactions in the window")
        void passesUnderLimit() {
            List<Transaction> recent = List.of(past(TEN, MERCHANT, 1), past(TEN, MERCHANT, 1));
            FraudAnalysisContext ctx = new FraudAnalysisContext(activeCard(), candidate(), recent);
            assertThat(rule.validate(ctx)).isTrue();
        }

        @Test
        @DisplayName("fails when more than three transactions happen within two minutes")
        void failsOverLimit() {
            List<Transaction> recent = List.of(
                    past(TEN, MERCHANT, 1), past(TEN, MERCHANT, 1), past(TEN, MERCHANT, 1));
            FraudAnalysisContext ctx = new FraudAnalysisContext(activeCard(), candidate(), recent);
            assertThat(rule.validate(ctx)).isFalse();
            assertThat(rule.violationCode()).isEqualTo("high-frequency-small-interval");
        }

        @Test
        @DisplayName("ignores transactions outside the two-minute window")
        void ignoresOutsideWindow() {
            List<Transaction> recent = List.of(
                    past(TEN, MERCHANT, 5), past(TEN, MERCHANT, 5), past(TEN, MERCHANT, 5));
            FraudAnalysisContext ctx = new FraudAnalysisContext(activeCard(), candidate(), recent);
            assertThat(rule.validate(ctx)).isTrue();
        }
    }

    @Nested
    @DisplayName("DoubledTransactionRule")
    class Doubled {

        private final DoubledTransactionRule rule = new DoubledTransactionRule();

        @Test
        @DisplayName("passes when fewer than two similar transactions exist")
        void passesUnderLimit() {
            List<Transaction> recent = List.of(past(TEN, MERCHANT, 1));
            FraudAnalysisContext ctx = new FraudAnalysisContext(activeCard(), candidate(), recent);
            assertThat(rule.validate(ctx)).isTrue();
        }

        @Test
        @DisplayName("fails with two or more similar transactions within two minutes")
        void failsOverLimit() {
            List<Transaction> recent = List.of(past(TEN, MERCHANT, 1), past(TEN, MERCHANT, 1));
            FraudAnalysisContext ctx = new FraudAnalysisContext(activeCard(), candidate(), recent);
            assertThat(rule.validate(ctx)).isFalse();
            assertThat(rule.violationCode()).isEqualTo("doubled-transaction");
        }

        @Test
        @DisplayName("different merchant or amount is not considered similar")
        void differentNotSimilar() {
            List<Transaction> recent = List.of(
                    past(TEN, "OTHER", 1), past(Money.of(new BigDecimal("20.00")), MERCHANT, 1));
            FraudAnalysisContext ctx = new FraudAnalysisContext(activeCard(), candidate(), recent);
            assertThat(rule.validate(ctx)).isTrue();
        }
    }
}
