package com.streaming.application.billing;

import com.streaming.domain.billing.Card;
import com.streaming.domain.billing.CardNumber;
import com.streaming.domain.billing.CardRepository;
import com.streaming.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CardApplicationService {

    private final CardRepository cardRepository;

    public CardApplicationService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Transactional
    public Card create(String number, String holder, String expiration, String cvv) {
        Card card = Card.create(CardNumber.of(number), holder, expiration, cvv);
        return cardRepository.save(card);
    }

    @Transactional
    public Card activate(Long id) {
        Card card = findById(id);
        card.activate();
        return cardRepository.save(card);
    }

    @Transactional
    public Card deactivate(Long id) {
        Card card = findById(id);
        card.deactivate();
        return cardRepository.save(card);
    }

    @Transactional(readOnly = true)
    public Card findById(Long id) {
        return cardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Card", id));
    }
}
