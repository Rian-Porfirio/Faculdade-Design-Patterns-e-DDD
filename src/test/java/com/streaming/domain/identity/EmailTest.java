package com.streaming.domain.identity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Email Value Object")
class EmailTest {

    @Test
    @DisplayName("normalizes to lower case and trims")
    void normalizes() {
        Email email = Email.of("  Alice@Example.COM ");

        assertThat(email.value()).isEqualTo("alice@example.com");
    }

    @ParameterizedTest
    @ValueSource(strings = {"plainaddress", "@no-local.com", "no-at.com", "spaces in@mail.com", "trailing@dot."})
    @DisplayName("rejects invalid formats")
    void rejectsInvalid(String invalid) {
        assertThatThrownBy(() -> Email.of(invalid))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("equality is based on normalized value")
    void equality() {
        assertThat(Email.of("Bob@Mail.com")).isEqualTo(Email.of("bob@mail.com"));
    }
}
