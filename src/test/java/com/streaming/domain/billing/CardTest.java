package com.streaming.domain.billing;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Card Aggregate")
class CardTest {

    private Card newCard() {
        return Card.create(CardNumber.of("4111111111111111"), "Alice", "12/2030", "123");
    }

    @Test
    @DisplayName("is created active")
    void createdActive() {
        assertThat(newCard().isActive()).isTrue();
        assertThat(newCard().status()).isEqualTo(CardStatus.ACTIVE);
    }

    @Test
    @DisplayName("transitions through lifecycle states")
    void lifecycle() {
        Card card = newCard();
        card.deactivate();
        assertThat(card.status()).isEqualTo(CardStatus.INACTIVE);
        card.block();
        assertThat(card.status()).isEqualTo(CardStatus.BLOCKED);
        card.activate();
        assertThat(card.isActive()).isTrue();
    }

    @Test
    @DisplayName("rejects invalid expiration format")
    void rejectsExpiration() {
        assertThatThrownBy(() -> Card.create(CardNumber.of("4111111111111111"), "Alice", "2030-12", "123"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("rejects invalid cvv")
    void rejectsCvv() {
        assertThatThrownBy(() -> Card.create(CardNumber.of("4111111111111111"), "Alice", "12/2030", "12"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
