package com.streaming.interfaces.billing;

import com.streaming.domain.billing.Card;

public record CardResponse(
        Long id,
        String number,
        String holder,
        String expiration,
        String status
) {
    public static CardResponse from(Card card) {
        return new CardResponse(
                card.id(),
                card.number().masked(),
                card.holder(),
                card.expiration(),
                card.status().name()
        );
    }
}
