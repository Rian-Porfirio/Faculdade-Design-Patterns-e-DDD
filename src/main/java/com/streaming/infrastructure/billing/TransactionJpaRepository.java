package com.streaming.infrastructure.billing;

import com.streaming.domain.billing.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionJpaRepository extends JpaRepository<TransactionJpaEntity, Long> {

    List<TransactionJpaEntity> findByCardIdAndStatusAndDateTimeGreaterThanEqual(
            Long cardId, TransactionStatus status, LocalDateTime since);
}
