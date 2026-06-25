package com.streaming.shared.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Money Value Object")
class MoneyTest {

    @Test
    @DisplayName("normalizes scale to two decimal places")
    void normalizesScale() {
        Money money = Money.of(new BigDecimal("10.5"));

        assertThat(money.amount()).isEqualByComparingTo("10.50");
        assertThat(money.currency()).isEqualTo("BRL");
    }

    @Test
    @DisplayName("uppercases the currency code")
    void uppercasesCurrency() {
        Money money = Money.of(new BigDecimal("10.00"), "usd");

        assertThat(money.currency()).isEqualTo("USD");
    }

    @Test
    @DisplayName("rejects negative amounts")
    void rejectsNegative() {
        assertThatThrownBy(() -> Money.of(new BigDecimal("-1.00")))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("rejects blank currency")
    void rejectsBlankCurrency() {
        assertThatThrownBy(() -> Money.of(new BigDecimal("1.00"), " "))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("considers same value and currency equal regardless of scale")
    void equalityIgnoresScale() {
        assertThat(Money.of(new BigDecimal("10.0")))
                .isEqualTo(Money.of(new BigDecimal("10.00")));
        assertThat(Money.of(new BigDecimal("10.00")).hasSameValueAs(Money.of(new BigDecimal("10.000"))))
                .isTrue();
    }

    @Test
    @DisplayName("different currencies are not the same value")
    void differentCurrenciesNotEqual() {
        assertThat(Money.of(new BigDecimal("10.00"), "BRL")
                .hasSameValueAs(Money.of(new BigDecimal("10.00"), "USD")))
                .isFalse();
    }
}
