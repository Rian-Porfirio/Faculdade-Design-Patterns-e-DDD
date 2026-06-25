package com.streaming.application.billing;

import com.streaming.domain.billing.Card;
import com.streaming.domain.billing.CardRepository;
import com.streaming.domain.billing.FraudAnalysisContext;
import com.streaming.domain.billing.FraudAnalysisService;
import com.streaming.domain.billing.Transaction;
import com.streaming.domain.billing.TransactionRepository;
import com.streaming.shared.application.DomainEventPublisher;
import com.streaming.shared.domain.Money;
import com.streaming.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionApplicationService {

    private static final int FRAUD_WINDOW_MINUTES = 2;

    private final CardRepository cardRepository;
    private final TransactionRepository transactionRepository;
    private final FraudAnalysisService fraudAnalysisService;
    private final DomainEventPublisher eventPublisher;

    public TransactionApplicationService(CardRepository cardRepository,
                                         TransactionRepository transactionRepository,
                                         FraudAnalysisService fraudAnalysisService,
                                         DomainEventPublisher eventPublisher) {
        this.cardRepository = cardRepository;
        this.transactionRepository = transactionRepository;
        this.fraudAnalysisService = fraudAnalysisService;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public Transaction create(Long cardId, BigDecimal amount, String currency, String merchant) {
        return authorize(cardId, Money.of(amount, currency), merchant);
    }

    @Transactional
    public Transaction authorize(Long cardId, Money amount, String merchant) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new ResourceNotFoundException("Card", cardId));

        LocalDateTime now = LocalDateTime.now();
        Transaction candidate = Transaction.newCharge(cardId, amount, merchant, now);

        List<Transaction> recent = transactionRepository.findAuthorizedByCardSince(
                cardId, now.minusMinutes(FRAUD_WINDOW_MINUTES));

        fraudAnalysisService.analyze(new FraudAnalysisContext(card, candidate, recent));

        candidate.authorize();
        Transaction saved = transactionRepository.save(candidate);
        eventPublisher.publishAll(candidate.pullDomainEvents());
        return saved;
    }

    @Transactional(readOnly = true)
    public Transaction findById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction", id));
    }
}
