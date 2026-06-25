package com.streaming.infrastructure.billing;

import com.streaming.domain.billing.Transaction;
import com.streaming.domain.billing.TransactionRepository;
import com.streaming.domain.billing.TransactionStatus;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class TransactionRepositoryAdapter implements TransactionRepository {

    private final TransactionJpaRepository jpaRepository;

    public TransactionRepositoryAdapter(TransactionJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Transaction save(Transaction transaction) {
        return TransactionMapper.toDomain(jpaRepository.save(TransactionMapper.toJpa(transaction)));
    }

    @Override
    public Optional<Transaction> findById(Long id) {
        return jpaRepository.findById(id).map(TransactionMapper::toDomain);
    }

    @Override
    public List<Transaction> findAuthorizedByCardSince(Long cardId, LocalDateTime since) {
        return jpaRepository
                .findByCardIdAndStatusAndDateTimeGreaterThanEqual(cardId, TransactionStatus.AUTHORIZED, since)
                .stream()
                .map(TransactionMapper::toDomain)
                .toList();
    }
}
