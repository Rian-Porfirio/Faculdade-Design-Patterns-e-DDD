package com.streaming.domain.billing;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository {

    Transaction save(Transaction transaction);

    Optional<Transaction> findById(Long id);

    List<Transaction> findAuthorizedByCardSince(Long cardId, LocalDateTime since);
}
