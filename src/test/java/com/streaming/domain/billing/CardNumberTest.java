package com.streaming.domain.billing;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("CardNumber Value Object")
class CardNumberTest {

    @Test
    @DisplayName("strips whitespace and keeps digits")
    void stripsWhitespace() {
        CardNumber number = CardNumber.of("4111 1111 1111 1111");

        assertThat(number.value()).isEqualTo("4111111111111111");
    }

    @Test
    @DisplayName("masks all but the last four digits")
    void masks() {
        CardNumber number = CardNumber.of("4111111111111111");

        assertThat(number.masked()).isEqualTo("**** **** **** 1111");
    }

    @Test
    @DisplayName("rejects too-short numbers")
    void rejectsShort() {
        assertThatThrownBy(() -> CardNumber.of("123"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("rejects non-digit numbers")
    void rejectsNonDigits() {
        assertThatThrownBy(() -> CardNumber.of("4111-ABCD-1111-2222"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
