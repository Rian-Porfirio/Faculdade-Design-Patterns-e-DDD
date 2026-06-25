package com.streaming.domain.subscription;

import com.streaming.shared.domain.Money;
import com.streaming.shared.exception.BusinessRuleViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("SubscriptionActivationService")
class SubscriptionActivationServiceTest {

    private final SubscriptionActivationService service = new SubscriptionActivationService();

    private Plan plan(Long id) {
        return Plan.reconstitute(id, "Premium", Money.of(new BigDecimal("19.90")), 30);
    }

    private Subscription activeSubscription(Long userId, Long planId) {
        return Subscription.reconstitute(1L, userId, planId, SubscriptionStatus.ACTIVE,
                LocalDate.now(), LocalDate.now().plusDays(30));
    }

    @Test
    @DisplayName("activates a plan when the user has no active subscription")
    void activatesWithoutPrevious() {
        Subscription activated = service.activate(7L, plan(2L), Optional.empty());

        assertThat(activated.isActive()).isTrue();
        assertThat(activated.userId()).isEqualTo(7L);
        assertThat(activated.planId()).isEqualTo(2L);
    }

    @Test
    @DisplayName("cancels the previous subscription when switching plans")
    void cancelsPreviousWhenSwitching() {
        Subscription previous = activeSubscription(7L, 2L);

        Subscription activated = service.activate(7L, plan(3L), Optional.of(previous));

        assertThat(previous.status()).isEqualTo(SubscriptionStatus.CANCELLED);
        assertThat(activated.planId()).isEqualTo(3L);
        assertThat(activated.isActive()).isTrue();
    }

    @Test
    @DisplayName("rejects re-subscribing to the same active plan")
    void rejectsSamePlan() {
        Subscription previous = activeSubscription(7L, 2L);

        assertThatThrownBy(() -> service.activate(7L, plan(2L), Optional.of(previous)))
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessage("active-subscription-already-exists");
        assertThat(previous.status()).isEqualTo(SubscriptionStatus.ACTIVE);
    }
}
