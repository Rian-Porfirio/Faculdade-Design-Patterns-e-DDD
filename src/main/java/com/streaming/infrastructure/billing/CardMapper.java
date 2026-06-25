package com.streaming.infrastructure.billing;

import com.streaming.domain.billing.Card;
import com.streaming.domain.billing.CardNumber;

final class CardMapper {

    private CardMapper() {
    }

    static CardJpaEntity toJpa(Card card) {
        return new CardJpaEntity(
                card.id(),
                card.number().value(),
                card.holder(),
                card.expiration(),
                card.cvv(),
                card.status());
    }

    static Card toDomain(CardJpaEntity entity) {
        return Card.reconstitute(
                entity.getId(),
                CardNumber.of(entity.getCardNumber()),
                entity.getHolder(),
                entity.getExpiration(),
                entity.getCvv(),
                entity.getStatus());
    }
}
