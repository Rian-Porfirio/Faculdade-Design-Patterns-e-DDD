package com.streaming.interfaces.billing;

import com.streaming.application.billing.TransactionApplicationService;
import com.streaming.shared.domain.Money;
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
@RequestMapping("/transactions")
@Tag(name = "Transactions", description = "Card charges processed through the antifraud engine")
public class TransactionController {

    private static final String DEFAULT_MERCHANT = "STREAMING-CHARGE";

    private final TransactionApplicationService transactionApplicationService;

    public TransactionController(TransactionApplicationService transactionApplicationService) {
        this.transactionApplicationService = transactionApplicationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Process a card charge through the antifraud engine")
    public TransactionResponse create(@Valid @RequestBody CreateTransactionRequest request) {
        String currency = (request.currency() == null || request.currency().isBlank())
                ? Money.DEFAULT_CURRENCY
                : request.currency();
        String merchant = (request.merchant() == null || request.merchant().isBlank())
                ? DEFAULT_MERCHANT
                : request.merchant();
        return TransactionResponse.from(transactionApplicationService.create(
                request.cardId(), request.amount(), currency, merchant));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find a transaction by id")
    public TransactionResponse findById(@PathVariable Long id) {
        return TransactionResponse.from(transactionApplicationService.findById(id));
    }
}
