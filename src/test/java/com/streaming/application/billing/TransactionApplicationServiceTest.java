package com.streaming.application.billing;

import com.streaming.domain.billing.Card;
import com.streaming.domain.billing.CardNumber;
import com.streaming.domain.billing.CardRepository;
import com.streaming.domain.billing.CardStatus;
import com.streaming.domain.billing.DoubledTransactionRule;
import com.streaming.domain.billing.FraudAnalysisService;
import com.streaming.domain.billing.HighFrequencySmallIntervalRule;
import com.streaming.domain.billing.InactiveCardRule;
import com.streaming.domain.billing.Transaction;
import com.streaming.domain.billing.TransactionRepository;
import com.streaming.domain.billing.TransactionStatus;
import com.streaming.shared.application.DomainEventPublisher;
import com.streaming.shared.domain.Money;
import com.streaming.shared.exception.BusinessRuleViolationException;
import com.streaming.shared.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("TransactionApplicationService")
class TransactionApplicationServiceTest {

    @Mock
    private CardRepository cardRepository;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private DomainEventPublisher eventPublisher;

    private TransactionApplicationService service;

    @BeforeEach
    void setUp() {
        FraudAnalysisService fraud = new FraudAnalysisService(List.of(
                new InactiveCardRule(),
                new HighFrequencySmallIntervalRule(),
                new DoubledTransactionRule()));
        service = new TransactionApplicationService(
                cardRepository, transactionRepository, fraud, eventPublisher);
    }

    private Card card(CardStatus status) {
        return Card.reconstitute(1L, CardNumber.of("4111111111111111"), "Alice",
                "12/2030", "123", status);
    }

    @Test
    @DisplayName("authorizes a charge on an active card and publishes events")
    void authorizesCharge() {
        when(cardRepository.findById(1L)).thenReturn(Optional.of(card(CardStatus.ACTIVE)));
        when(transactionRepository.findAuthorizedByCardSince(eq(1L), any())).thenReturn(List.of());
        when(transactionRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Transaction result = service.create(1L, new BigDecimal("19.90"), "BRL", "STREAMING");

        assertThat(result.status()).isEqualTo(TransactionStatus.AUTHORIZED);
        ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(captor.capture());
        assertThat(captor.getValue().isAuthorized()).isTrue();
        verify(eventPublisher).publishAll(any());
    }

    @Test
    @DisplayName("rejects a charge on an inactive card")
    void rejectsInactiveCard() {
        when(cardRepository.findById(1L)).thenReturn(Optional.of(card(CardStatus.INACTIVE)));
        when(transactionRepository.findAuthorizedByCardSince(eq(1L), any())).thenReturn(List.of());

        assertThatThrownBy(() -> service.create(1L, new BigDecimal("19.90"), "BRL", "STREAMING"))
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessage("inactive-card");

        verify(transactionRepository, never()).save(any());
        verify(eventPublisher, never()).publishAll(any());
    }

    @Test
    @DisplayName("rejects a doubled transaction within the window")
    void rejectsDoubled() {
        Money amount = Money.of(new BigDecimal("19.90"));
        List<Transaction> recent = List.of(
                Transaction.reconstitute(2L, 1L, amount, "STREAMING",
                        LocalDateTime.now().minusSeconds(30), TransactionStatus.AUTHORIZED),
                Transaction.reconstitute(3L, 1L, amount, "STREAMING",
                        LocalDateTime.now().minusSeconds(20), TransactionStatus.AUTHORIZED));
        when(cardRepository.findById(1L)).thenReturn(Optional.of(card(CardStatus.ACTIVE)));
        when(transactionRepository.findAuthorizedByCardSince(eq(1L), any())).thenReturn(recent);

        assertThatThrownBy(() -> service.create(1L, new BigDecimal("19.90"), "BRL", "STREAMING"))
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessage("doubled-transaction");
    }

    @Test
    @DisplayName("fails when the card does not exist")
    void failsMissingCard() {
        when(cardRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.create(99L, new BigDecimal("19.90"), "BRL", "STREAMING"))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
