package com.streaming.config;

import com.streaming.domain.billing.DoubledTransactionRule;
import com.streaming.domain.billing.FraudAnalysisService;
import com.streaming.domain.billing.FraudRule;
import com.streaming.domain.billing.HighFrequencySmallIntervalRule;
import com.streaming.domain.billing.InactiveCardRule;
import com.streaming.domain.subscription.SubscriptionActivationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DomainServicesConfig {

    @Bean
    public FraudAnalysisService fraudAnalysisService() {
        List<FraudRule> rules = List.of(
                new InactiveCardRule(),
                new HighFrequencySmallIntervalRule(),
                new DoubledTransactionRule());
        return new FraudAnalysisService(rules);
    }

    @Bean
    public SubscriptionActivationService subscriptionActivationService() {
        return new SubscriptionActivationService();
    }
}
