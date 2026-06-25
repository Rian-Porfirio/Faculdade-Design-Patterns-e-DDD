package com.streaming.interfaces.billing;

import com.streaming.application.billing.CardApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cards")
@Tag(name = "Cards", description = "Credit card registration and status management")
public class CardController {

    private final CardApplicationService cardApplicationService;

    public CardController(CardApplicationService cardApplicationService) {
        this.cardApplicationService = cardApplicationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register a new credit card")
    public CardResponse create(@Valid @RequestBody CreateCardRequest request) {
        return CardResponse.from(cardApplicationService.create(
                request.number(), request.holder(), request.expiration(), request.cvv()));
    }

    @PatchMapping("/{id}/activate")
    @Operation(summary = "Activate a credit card")
    public CardResponse activate(@PathVariable Long id) {
        return CardResponse.from(cardApplicationService.activate(id));
    }

    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate a credit card")
    public CardResponse deactivate(@PathVariable Long id) {
        return CardResponse.from(cardApplicationService.deactivate(id));
    }
}
