package com.streaming.interfaces.subscription;

import com.streaming.application.subscription.SubscriptionApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/subscriptions")
@Tag(name = "Subscriptions", description = "Plan subscriptions backed by a valid charge (Partnership with Billing)")
public class SubscriptionController {

    private final SubscriptionApplicationService subscriptionApplicationService;

    public SubscriptionController(SubscriptionApplicationService subscriptionApplicationService) {
        this.subscriptionApplicationService = subscriptionApplicationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Subscribe a user to a plan, charging the plan price on a card")
    public SubscriptionResponse subscribe(@Valid @RequestBody CreateSubscriptionRequest request) {
        return SubscriptionResponse.from(subscriptionApplicationService.subscribe(
                request.userId(), request.planId(), request.cardId()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find a subscription by id")
    public SubscriptionResponse findById(@PathVariable Long id) {
        return SubscriptionResponse.from(subscriptionApplicationService.findById(id));
    }
}
