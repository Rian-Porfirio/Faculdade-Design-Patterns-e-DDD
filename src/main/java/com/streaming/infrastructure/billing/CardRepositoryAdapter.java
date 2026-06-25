package com.streaming.infrastructure.billing;

import com.streaming.domain.billing.Card;
import com.streaming.domain.billing.CardRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CardRepositoryAdapter implements CardRepository {

    private final CardJpaRepository jpaRepository;

    public CardRepositoryAdapter(CardJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Card save(Card card) {
        return CardMapper.toDomain(jpaRepository.save(CardMapper.toJpa(card)));
    }

    @Override
    public Optional<Card> findById(Long id) {
        return jpaRepository.findById(id).map(CardMapper::toDomain);
    }
}
