package com.streaming.application.subscription;

import com.streaming.domain.identity.Email;
import com.streaming.domain.identity.User;
import com.streaming.domain.identity.UserRepository;
import com.streaming.domain.subscription.Plan;
import com.streaming.domain.subscription.PlanRepository;
import com.streaming.domain.subscription.Subscription;
import com.streaming.domain.subscription.SubscriptionActivationService;
import com.streaming.domain.subscription.SubscriptionRepository;
import com.streaming.domain.subscription.SubscriptionStatus;
import com.streaming.shared.application.DomainEventPublisher;
import com.streaming.shared.domain.Money;
import com.streaming.shared.exception.BusinessRuleViolationException;
import com.streaming.shared.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("SubscriptionApplicationService")
class SubscriptionApplicationServiceTest {

    @Mock
    private SubscriptionRepository subscriptionRepository;
    @Mock
    private PlanRepository planRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ChargePort chargePort;
    @Mock
    private DomainEventPublisher eventPublisher;

    private SubscriptionApplicationService service;

    @BeforeEach
    void setUp() {
        service = new SubscriptionApplicationService(
                subscriptionRepository, planRepository, userRepository,
                new SubscriptionActivationService(), chargePort, eventPublisher);
    }

    private User user() {
        return User.reconstitute(1L, "Alice", Email.of("alice@example.com"), java.util.Set.of());
    }

    private Plan plan(Long id) {
        return Plan.reconstitute(id, "Premium", Money.of(new BigDecimal("19.90")), 30);
    }

    @Test
    @DisplayName("charges the plan price before activating and publishes events")
    void chargesThenActivates() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user()));
        when(planRepository.findById(2L)).thenReturn(Optional.of(plan(2L)));
        when(subscriptionRepository.findActiveByUserId(1L)).thenReturn(Optional.empty());
        when(chargePort.charge(eq(5L), any(Money.class), any())).thenReturn(100L);
        when(subscriptionRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Subscription result = service.subscribe(1L, 2L, 5L);

        assertThat(result.isActive()).isTrue();
        verify(chargePort).charge(eq(5L), eq(Money.of(new BigDecimal("19.90"))), any());
        verify(eventPublisher).publishAll(any());
    }

    @Test
    @DisplayName("cancels the previous active subscription when switching plans")
    void cancelsPrevious() {
        Subscription previous = Subscription.reconstitute(9L, 1L, 2L, SubscriptionStatus.ACTIVE,
                LocalDate.now(), LocalDate.now().plusDays(30));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user()));
        when(planRepository.findById(3L)).thenReturn(Optional.of(plan(3L)));
        when(subscriptionRepository.findActiveByUserId(1L)).thenReturn(Optional.of(previous));
        when(chargePort.charge(any(), any(), any())).thenReturn(100L);
        when(subscriptionRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        service.subscribe(1L, 3L, 5L);

        assertThat(previous.status()).isEqualTo(SubscriptionStatus.CANCELLED);
        verify(subscriptionRepository).save(previous);
    }

    @Test
    @DisplayName("does not charge when the user does not exist")
    void failsMissingUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.subscribe(1L, 2L, 5L))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(chargePort, never()).charge(any(), any(), any());
    }

    @Test
    @DisplayName("propagates antifraud rejection from the charge port")
    void propagatesChargeRejection() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user()));
        when(planRepository.findById(2L)).thenReturn(Optional.of(plan(2L)));
        when(subscriptionRepository.findActiveByUserId(1L)).thenReturn(Optional.empty());
        when(chargePort.charge(any(), any(), any()))
                .thenThrow(new BusinessRuleViolationException("inactive-card"));

        assertThatThrownBy(() -> service.subscribe(1L, 2L, 5L))
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessage("inactive-card");

        verify(subscriptionRepository, never()).save(any());
    }
}
